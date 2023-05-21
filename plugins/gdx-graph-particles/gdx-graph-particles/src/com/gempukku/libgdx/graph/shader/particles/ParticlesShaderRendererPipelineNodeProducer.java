package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.producer.DefaultShaderContext;

public class ParticlesShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ParticlesShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final DefaultShaderContext shaderContext = new DefaultShaderContext(pipelineDataProvider.getRootPropertyContainer(), pluginPrivateDataSource, pipelineDataProvider.getWhitePixel().textureRegion);

        final Array<GraphShader> particleShaders = new Array<>();
        final JsonValue shaderDefinitions = data.get("shaders");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private FullScreenRender fullScreenRender;
            private TimeProvider timeProvider;
            private GraphModelsImpl particleEffects;

            @Override
            public void initializePipeline() {
                fullScreenRender = pipelineDataProvider.getFullScreenRender();
                timeProvider = pipelineDataProvider.getTimeProvider();
                particleEffects = pipelineDataProvider.getPrivatePluginData(GraphModelsImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    String tag = shaderDefinition.getString("tag");
                    FileHandle shaderPath = pipelineDataProvider.getAssetResolver().resolve(shaderDefinition.getString("path"));
                    Gdx.app.debug("Shader", "Building shader with tag: " + tag);
                    final GraphShader graphShader = ParticleShaderLoader.loadShader(shaderPath, tag, pipelineDataProvider.getAssetResolver());
                    particleShaders.add(graphShader);
                }

                for (GraphShader particleShader : particleShaders) {
                    particleEffects.registerTag(particleShader.getTag(), particleShader);
                }
            }

            private boolean usesDepth() {
                for (GraphShader particleShader : particleShaders) {
                    if (particleShader.isUsingDepthTexture()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputs.get("camera");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                if (enabled) {
                    boolean usesDepth = usesDepth();

                    boolean needsSceneColor = false;
                    for (GraphShader particleShader : particleShaders) {
                        if (particleShader.isUsingColorTexture()) {
                            needsSceneColor = true;
                            break;
                        }
                    }

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    if (usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                    }

                    if (cameraInput != null) {
                        Camera camera = cameraInput.getValue();
                        shaderContext.setCamera(camera);
                    }

                    shaderContext.setTimeProvider(timeProvider);
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    currentBuffer.beginColor();

                    for (GraphShader particleShader : particleShaders) {
                        String tag = particleShader.getTag();
                        shaderContext.setGlobalPropertyContainer(particleEffects.getGlobalProperties(tag));
                        particleShader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                        for (RenderableModel model : particleEffects.getModels()) {
                            if (model.isRendered(particleShader, shaderContext.getCamera())) {
                                particleShader.render(shaderContext, model);
                            }
                        }
                        particleShader.end();
                    }

                    currentBuffer.endColor();

                    if (sceneColorBuffer != null)
                        renderPipeline.returnFrameBuffer(sceneColorBuffer);
                }

                output.setValue(renderPipeline);
            }

            private RenderPipelineBuffer setupColorTexture(final RenderPipeline renderPipeline, final RenderPipelineBuffer currentBuffer,
                                                           PipelineRenderingContext pipelineRenderingContext) {
                RenderPipelineBuffer sceneColorBuffer = renderPipeline.getNewFrameBuffer(currentBuffer, Color.BLACK);
                shaderContext.setColorTexture(sceneColorBuffer.getColorBufferTexture());
                renderPipeline.drawTexture(currentBuffer, sceneColorBuffer, pipelineRenderingContext, fullScreenRender);
                return sceneColorBuffer;
            }

            @Override
            public void dispose() {
                for (GraphShader particleShader : particleShaders) {
                    particleShader.dispose();
                }
            }
        };
    }
}

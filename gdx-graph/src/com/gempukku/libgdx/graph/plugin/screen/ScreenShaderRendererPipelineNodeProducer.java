package com.gempukku.libgdx.graph.plugin.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class ScreenShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ScreenShaderConfiguration()};
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ScreenShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final ShaderContextImpl shaderContext = new ShaderContextImpl(pluginPrivateDataSource);

        final Array<ScreenGraphShader> shaderArray = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private TimeProvider timeProvider;
            private FullScreenRender fullScreenRender;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                fullScreenRender = pipelineDataProvider.getFullScreenRender();
                timeProvider = pipelineDataProvider.getTimeProvider();
                GraphScreenShadersImpl graphScreenShaders = pipelineDataProvider.getPrivatePluginData(GraphScreenShadersImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    JsonValue shaderGraph = shaderDefinition.get("shader");
                    String tag = shaderDefinition.getString("tag");
                    Gdx.app.debug("Shader", "Building shader with tag: " + tag);
                    final ScreenGraphShader shader = GraphLoader.loadGraph(shaderGraph, new ScreenShaderLoaderCallback(tag, pipelineDataProvider.getWhitePixel().texture, configurations), PropertyLocation.Global_Uniform);
                    shaderArray.add(shader);
                }

                for (ScreenGraphShader shader : shaderArray) {
                    graphScreenShaders.registerTag(shader.getTag(), shader);
                }
            }

            private boolean needsDepth() {
                for (ScreenGraphShader shader : shaderArray) {
                    if (shader.isUsingDepthTexture())
                        return true;
                }
                return false;
            }

            private boolean isRequiringSceneColor() {
                for (ScreenGraphShader shader : shaderArray) {
                    if (shader.isUsingColorTexture())
                        return true;
                }
                return false;
            }

            @Override
            public void processPipelineRequirements(PipelineRequirements pipelineRequirements) {
                if (needsDepth())
                    pipelineRequirements.setRequiringDepthTexture();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputs.get("camera");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                if (enabled) {
                    boolean usesDepth = needsDepth();

                    boolean needsSceneColor = isRequiringSceneColor();

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

                    for (ScreenGraphShader shader : shaderArray) {
                        shaderContext.setGlobalPropertyContainer(shader.getPropertyContainer());
                        shader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                        shader.render(shaderContext, fullScreenRender);
                        shader.end();
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
                for (ScreenGraphShader shader : shaderArray) {
                    shader.dispose();
                }
            }
        };
    }
}

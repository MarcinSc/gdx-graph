package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
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
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.shader.lighting3d.ShadowShaderLoader;
import com.gempukku.libgdx.graph.shader.producer.DefaultShaderContext;
import com.gempukku.libgdx.graph.shader.strategy.*;

public class ShadowShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ShadowShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ShadowShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final DefaultShaderContext shaderContext = new DefaultShaderContext(pipelineDataProvider.getRootPropertyContainer(), pluginPrivateDataSource, pipelineDataProvider.getWhitePixel().textureRegion);

        final Array<GraphShader> shaders = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final ModelRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final String environmentId = data.getString("id", "");

        final RenderingStrategyCallback depthStrategyCallback = new RenderingStrategyCallback(
                shaderContext);

        final Array<RenderPipelineBuffer> createdPipelineBuffers = new Array<>();
        final Array<Directional3DLight> shadowDirectionalLights = new Array<>();

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private Lighting3DPrivateData lighting;
            private TimeProvider timeProvider;
            private GraphModelsImpl models;
            private RenderPipeline pipeline;

            @Override
            public void initializePipeline() {
                lighting = pipelineDataProvider.getPrivatePluginData(Lighting3DPrivateData.class);
                timeProvider = pipelineDataProvider.getTimeProvider();
                models = pipelineDataProvider.getPrivatePluginData(GraphModelsImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    GraphShader depthGraphShader = ShadowShaderRendererPipelineNodeProducer.createDepthShader(pipelineDataProvider.getAssetResolver(), shaderDefinition);

                    shaders.add(depthGraphShader);
                    models.registerTag(depthGraphShader.getTag(), depthGraphShader);
                }
            }

            private boolean needsDepth() {
                for (GraphShader shader : shaders) {
                    if (shader.isUsingDepthTexture())
                        return true;
                }
                return false;
            }

            private boolean isRequiringSceneColor() {
                for (GraphShader shader : shaders) {
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
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                boolean usesDepth = enabled && needsDepth();

                RenderPipeline renderPipeline = renderPipelineInput.getValue();
                this.pipeline = renderPipeline;

                if (enabled) {
                    boolean needsDrawing = false;
                    Lighting3DEnvironment environment = lighting.getEnvironment(environmentId);
                    // Initialize directional light cameras and textures
                    for (Directional3DLight directionalLight : environment.getDirectionalLights()) {
                        if (directionalLight.isShadowsEnabled()) {
                            needsDrawing = true;
                            directionalLight.updateCamera(environment.getSceneCenter(), environment.getSceneDiameter());
                            if (directionalLight.getShadowFrameBuffer() == null) {
                                RenderPipelineBuffer shadowFrameBuffer = renderPipeline.getNewFrameBuffer(directionalLight.getShadowBufferSize(), directionalLight.getShadowBufferSize(), Pixmap.Format.RGB888, Color.WHITE);
                                directionalLight.setShadowFrameBuffer(shadowFrameBuffer);
                                createdPipelineBuffers.add(shadowFrameBuffer);
                                shadowDirectionalLights.add(directionalLight);
                            }
                        }
                    }

                    if (needsDrawing) {
                        boolean needsSceneColor = isRequiringSceneColor();

                        RenderPipelineBuffer drawBuffer = renderPipeline.getDefaultBuffer();

                        for (Directional3DLight directionalLight : environment.getDirectionalLights()) {
                            if (directionalLight.isShadowsEnabled()) {
                                RenderPipelineBuffer shadowBuffer = directionalLight.getShadowFrameBuffer();
                                Camera camera = directionalLight.getShadowCamera();

                                shaderContext.setCamera(camera);
                                shaderContext.setTimeProvider(timeProvider);
                                shaderContext.setRenderWidth(shadowBuffer.getWidth());
                                shaderContext.setRenderHeight(shadowBuffer.getHeight());

                                if (usesDepth) {
                                    renderPipeline.enrichWithDepthBuffer(drawBuffer);
                                    shaderContext.setDepthTexture(drawBuffer.getDepthBufferTexture());
                                }

                                if (needsSceneColor)
                                    shaderContext.setColorTexture(drawBuffer.getColorBufferTexture());

                                // Drawing models on color buffer
                                depthStrategyCallback.prepare(pipelineRenderingContext, models);
                                shadowBuffer.beginColor();
                                renderingStrategy.processModels(models, shaders, camera, depthStrategyCallback);
                                shadowBuffer.endColor();
                            }
                        }
                    }
                }

                output.setValue(renderPipeline);
            }

            @Override
            public void endFrame() {
                for (RenderPipelineBuffer createdPipelineBuffer : createdPipelineBuffers) {
                    pipeline.returnFrameBuffer(createdPipelineBuffer);
                }
                createdPipelineBuffers.clear();
                for (Directional3DLight shadowDirectionalLight : shadowDirectionalLights) {
                    shadowDirectionalLight.setShadowFrameBuffer(null);
                }
                shadowDirectionalLights.clear();
            }

            @Override
            public void dispose() {
                for (GraphShader shader : shaders) {
                    shader.dispose();
                }
            }
        };
    }

    private ModelRenderingStrategy createRenderingStrategy(RenderOrder renderOrder) {
        switch (renderOrder) {
            case Shader_Unordered:
                return new ShaderUnorderedModelRenderingStrategy();
            case Shader_Back_To_Front:
                return new ShaderBackToFrontModelRenderingStrategy();
            case Shader_Front_To_Back:
                return new ShaderFrontToBackModelRenderingStrategy();
            case Back_To_Front:
                return new BackToFrontModelRenderingStrategy();
            case Front_To_Back:
                return new FrontToBackModelRenderingStrategy();
        }
        throw new IllegalStateException("Unrecognized RenderOrder: " + renderOrder.name());
    }

    private static GraphShader createDepthShader(FileHandleResolver assetResolver, JsonValue shaderDefinition) {
        FileHandle shaderFile = assetResolver.resolve(shaderDefinition.getString("path"));
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return ShadowShaderLoader.loadShader(shaderFile, tag, assetResolver);
    }

    private static class RenderingStrategyCallback implements ModelRenderingStrategy.StrategyCallback {
        private final DefaultShaderContext shaderContext;

        private GraphModelsImpl graphModels;
        private PipelineRenderingContext context;
        private GraphShader runningShader = null;

        public RenderingStrategyCallback(DefaultShaderContext shaderContext) {
            this.shaderContext = shaderContext;
        }

        public void prepare(PipelineRenderingContext context, GraphModelsImpl graphModels) {
            this.context = context;
            this.graphModels = graphModels;
        }

        @Override
        public void begin() {

        }

        @Override
        public void process(RenderableModel model, GraphShader shader) {
            if (runningShader != shader) {
                endCurrentShader();
                beginShader(shader);
            }
            shader.render(shaderContext, model);
        }

        private void beginShader(GraphShader shader) {
            shaderContext.setGlobalPropertyContainer(graphModels.getGlobalProperties(shader.getTag()));
            shader.begin(shaderContext, context.getRenderContext());
            runningShader = shader;
        }

        private void endCurrentShader() {
            if (runningShader != null)
                runningShader.end();
        }

        @Override
        public void end() {
            endCurrentShader();
            runningShader = null;
        }
    }
}
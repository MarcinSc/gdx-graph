package com.gempukku.libgdx.graph.shader.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelShaderLoader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.shader.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.strategy.*;

public class ModelShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ModelShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ModelShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        final DefaultShaderContext shaderContext = new DefaultShaderContext(pipelineDataProvider.getRootPropertyContainer(), pluginPrivateDataSource, pipelineDataProvider.getWhitePixel().textureRegion);

        final Array<ShaderGroup> shaderGroups = new Array<>();
        final Array<GraphShader> colorShaders = new Array<>();
        final Array<ShaderGroup> depthDrawingShaderGroups = new Array<>();
        final Array<GraphShader> depthShaders = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final ModelRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final RenderingStrategyCallback colorStrategyCallback = new RenderingStrategyCallback(shaderContext);

        final RenderingStrategyCallback depthStrategyCallback = new RenderingStrategyCallback(shaderContext);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private FullScreenRender fullScreenRender;
            private TimeProvider timeProvider;
            private GraphModelsImpl models;

            @Override
            public void initializePipeline() {
                fullScreenRender = pipelineDataProvider.getFullScreenRender();
                timeProvider = pipelineDataProvider.getTimeProvider();
                models = pipelineDataProvider.getPrivatePluginData(GraphModelsImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    ShaderGroup shaderGroup = new ShaderGroup(pipelineDataProvider.getAssetResolver(), shaderDefinition);
                    shaderGroup.initialize();

                    shaderGroups.add(shaderGroup);
                    colorShaders.add(shaderGroup.getColorShader());
                    models.registerTag(shaderGroup.getTag(), shaderGroup.getColorShader());

                    if (shaderGroup.getColorShader().isDepthWriting()) {
                        depthDrawingShaderGroups.add(shaderGroup);
                    }
                }
            }

            private void initializeDepthShaders() {
                for (ShaderGroup depthDrawingShaderGroup : depthDrawingShaderGroups) {
                    depthDrawingShaderGroup.initializeDepthShader();
                    depthShaders.add(depthDrawingShaderGroup.getDepthShader());
                }
            }

            private boolean needsDepth() {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture())
                        return true;
                }
                return false;
            }

            private boolean isRequiringSceneColor() {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingColorTexture())
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

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                if (enabled) {
                    boolean needsToDrawDepth = pipelineRequirementsCallback.getPipelineRequirements().isRequiringDepthTexture();
                    if (needsToDrawDepth)
                        initializeDepthShaders();

                    boolean usesDepth = needsDepth();

                    boolean needsSceneColor = isRequiringSceneColor();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                    Camera camera = cameraInput.getValue();

                    shaderContext.setCamera(camera);
                    shaderContext.setTimeProvider(timeProvider);
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    if (needsToDrawDepth || usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                        shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                    }

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor)
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);

                    // Drawing models on color buffer
                    colorStrategyCallback.prepare(pipelineRenderingContext, models);
                    currentBuffer.beginColor();
                    renderingStrategy.processModels(models, colorShaders, camera, colorStrategyCallback);
                    currentBuffer.endColor();

                    if (needsToDrawDepth) {
                        // Drawing models on depth buffer
                        depthStrategyCallback.prepare(pipelineRenderingContext, models);
                        currentBuffer.beginDepth();
                        renderingStrategy.processModels(models, depthShaders, camera, depthStrategyCallback);
                        currentBuffer.endDepth();
                    }

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
                for (ShaderGroup shaderGroup : shaderGroups) {
                    shaderGroup.dispose();
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

    private static GraphShader createColorShader(FileHandleResolver assetResolver, JsonValue shaderDefinition) {
        FileHandle graphFile = assetResolver.resolve(shaderDefinition.getString("path"));
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return ModelShaderLoader.loadShader(graphFile, tag, false, assetResolver);
    }

    private static GraphShader createDepthShader(FileHandleResolver assetResolver, JsonValue shaderDefinition) {
        FileHandle graphFile = assetResolver.resolve(shaderDefinition.getString("path"));
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return ModelShaderLoader.loadShader(graphFile, tag, true, assetResolver);
    }

    private static class ShaderGroup implements Disposable {
        private final FileHandleResolver assetResolver;
        private final JsonValue shaderDefinition;
        private GraphShader colorShader;
        private GraphShader depthShader;

        public ShaderGroup(FileHandleResolver assetResolver, JsonValue shaderDefinition) {
            this.assetResolver = assetResolver;
            this.shaderDefinition = shaderDefinition;
        }

        public void initialize() {
            colorShader = ModelShaderRendererPipelineNodeProducer.createColorShader(assetResolver, shaderDefinition);
        }

        public void initializeDepthShader() {
            if (depthShader == null && colorShader.isDepthWriting()) {
                depthShader = ModelShaderRendererPipelineNodeProducer.createDepthShader(assetResolver, shaderDefinition);
            }
        }

        public GraphShader getColorShader() {
            return colorShader;
        }

        public GraphShader getDepthShader() {
            return depthShader;
        }

        public String getTag() {
            return colorShader.getTag();
        }

        @Override
        public void dispose() {
            colorShader.dispose();
            if (depthShader != null)
                depthShader.dispose();
        }
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

                shaderContext.setGlobalPropertyContainer(graphModels.getGlobalProperties(shader.getTag()));
                shader.begin(shaderContext, context.getRenderContext());
                runningShader = shader;
            }
            shader.render(shaderContext, model);
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

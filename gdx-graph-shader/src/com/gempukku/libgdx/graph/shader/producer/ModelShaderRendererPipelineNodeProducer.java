package com.gempukku.libgdx.graph.shader.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelShaderGraphType;
import com.gempukku.libgdx.graph.shader.ModelShaderLoader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.shader.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.shader.depth.DepthShaderLoader;
import com.gempukku.libgdx.graph.shader.strategy.*;

public class ModelShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ModelShaderRendererPipelineNodeProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final ShaderRendererConfiguration shaderRendererConfiguration = configuration.getConfig(ShaderRendererConfiguration.class);
        if (shaderRendererConfiguration == null)
            throw new GdxRuntimeException("A configuration with class ShaderRendererConfiguration needs to be define for pipeline");

        final DefaultShaderContext shaderContext = new DefaultShaderContext();
        shaderContext.setPipelineRendererConfiguration(configuration);

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

        return new SingleInputsPipelineNode(result, configuration) {
            private FullScreenRender fullScreenRender;
            private TimeProvider timeProvider;

            @Override
            public void initializePipeline() {
                fullScreenRender = configuration.getPipelineHelper().getFullScreenRender();
                timeProvider = configuration.getTimeProvider();

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    ShaderGroup shaderGroup = new ShaderGroup(configuration, shaderDefinition);
                    shaderGroup.initialize();

                    shaderGroups.add(shaderGroup);
                    colorShaders.add(shaderGroup.getColorShader());
                    shaderRendererConfiguration.registerShader(shaderGroup.getColorShader());

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
                    colorStrategyCallback.prepare(pipelineRenderingContext, shaderRendererConfiguration);
                    currentBuffer.beginColor();
                    renderingStrategy.processModels(shaderRendererConfiguration, colorShaders, camera, colorStrategyCallback);
                    currentBuffer.endColor();

                    if (needsToDrawDepth) {
                        // Drawing models on depth buffer
                        depthStrategyCallback.prepare(pipelineRenderingContext, shaderRendererConfiguration);
                        currentBuffer.beginDepth();
                        renderingStrategy.processModels(shaderRendererConfiguration, depthShaders, camera, depthStrategyCallback);
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

    private static GraphShader createColorShader(PipelineRendererConfiguration configuration, JsonValue shaderDefinition) {
        FileHandle graphFile = configuration.getAssetResolver().resolve(shaderDefinition.getString("path"));
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return ModelShaderLoader.loadShader(graphFile, tag, configuration);
    }

    private static GraphShader createDepthShader(PipelineRendererConfiguration configuration, JsonValue shaderDefinition) {
        FileHandle graphFile = configuration.getAssetResolver().resolve(shaderDefinition.getString("path"));
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return DepthShaderLoader.loadShader(graphFile, ModelShaderGraphType.TYPE, tag, configuration);
    }

    private static class ShaderGroup implements Disposable {
        private final JsonValue shaderDefinition;
        private final PipelineRendererConfiguration configuration;
        private GraphShader colorShader;
        private GraphShader depthShader;

        public ShaderGroup(PipelineRendererConfiguration configuration, JsonValue shaderDefinition) {
            this.configuration = configuration;
            this.shaderDefinition = shaderDefinition;
        }

        public void initialize() {
            colorShader = ModelShaderRendererPipelineNodeProducer.createColorShader(configuration, shaderDefinition);
        }

        public void initializeDepthShader() {
            if (depthShader == null && colorShader.isDepthWriting()) {
                depthShader = ModelShaderRendererPipelineNodeProducer.createDepthShader(configuration, shaderDefinition);
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

        private ShaderRendererConfiguration configuration;
        private PipelineRenderingContext context;
        private GraphShader runningShader = null;

        public RenderingStrategyCallback(DefaultShaderContext shaderContext) {
            this.shaderContext = shaderContext;
        }

        public void prepare(PipelineRenderingContext context, ShaderRendererConfiguration configuration) {
            this.context = context;
            this.configuration = configuration;
        }

        @Override
        public void begin() {

        }

        @Override
        public void process(Object model, GraphShader shader) {
            if (runningShader != shader) {
                endCurrentShader();

                shaderContext.setGraphShader(shader);
                shaderContext.setGlobalPropertyContainer(configuration.getGlobalUniforms(shader));
                shader.begin(shaderContext, context.getRenderContext());
                runningShader = shader;
            }
            shader.render(configuration, shaderContext, model);
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

package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderLoaderCallback;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.strategy.*;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.util.function.Function;

public class ShadowShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ModelShaderConfiguration()};
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ShadowShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ShadowShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final ShaderContextImpl shaderContext = new ShaderContextImpl(pluginPrivateDataSource);

        final ObjectMap<String, GraphShader> shaders = new ObjectMap<>();
        final Array<String> allShaderTags = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final ModelRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final String environmentId = data.getString("id", "");

        final RenderingStrategyCallback depthStrategyCallback = new RenderingStrategyCallback(
                shaderContext, new Function<String, GraphShader>() {
            @Override
            public GraphShader apply(String s) {
                return shaders.get(s);
            }
        });

        final Array<RenderPipelineBuffer> createdPipelineBuffers = new Array<>();
        final Array<Directional3DLight> shadowDirectionalLights = new Array<>();

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private Lighting3DPrivateData lighting;
            private TimeProvider timeProvider;
            private GraphModelsImpl models;
            private RenderPipeline pipeline;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                lighting = pipelineDataProvider.getPrivatePluginData(Lighting3DPrivateData.class);
                timeProvider = pipelineDataProvider.getTimeProvider();
                models = pipelineDataProvider.getPrivatePluginData(GraphModelsImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    GraphShader depthGraphShader = ShadowShaderRendererPipelineNodeProducer.createDepthShader(shaderDefinition, pipelineDataProvider.getWhitePixel().texture);

                    allShaderTags.add(depthGraphShader.getTag());
                    shaders.put(depthGraphShader.getTag(), depthGraphShader);
                }

                for (ObjectMap.Entry<String, GraphShader> shaderEntry : shaders.entries()) {
                    models.registerTag(shaderEntry.key, shaderEntry.value);
                }
            }

            private boolean needsDepth() {
                for (GraphShader shader : shaders.values()) {
                    if (shader.isUsingDepthTexture() && models.hasModelWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            private boolean isRequiringSceneColor() {
                for (GraphShader shader : shaders.values()) {
                    if (shader.isUsingColorTexture() && models.hasModelWithTag(shader.getTag()))
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
                                renderingStrategy.processModels(models, allShaderTags, camera, depthStrategyCallback);
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
                for (GraphShader shader : shaders.values()) {
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

    private static GraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(tag, defaultTexture, true, configurations), PropertyLocation.Uniform);
    }

    private static class RenderingStrategyCallback implements ModelRenderingStrategy.StrategyCallback {
        private final ShaderContextImpl shaderContext;
        private final Function<String, GraphShader> shaderResolver;

        private GraphModelsImpl graphModels;
        private PipelineRenderingContext context;
        private GraphShader runningShader = null;

        public RenderingStrategyCallback(ShaderContextImpl shaderContext, Function<String, GraphShader> shaderResolver) {
            this.shaderContext = shaderContext;
            this.shaderResolver = shaderResolver;
        }

        public void prepare(PipelineRenderingContext context, GraphModelsImpl graphModels) {
            this.context = context;
            this.graphModels = graphModels;
        }

        @Override
        public void begin() {

        }

        @Override
        public void process(RenderableModel model, String tag) {
            GraphShader shader = shaderResolver.apply(tag);
            if (runningShader != shader) {
                endCurrentShader();
                beginShader(tag, shader);
            }
            shader.render(shaderContext, model);
        }

        private void beginShader(String tag, GraphShader shader) {
            shaderContext.setGlobalPropertyContainer(graphModels.getGlobalProperties(tag));
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

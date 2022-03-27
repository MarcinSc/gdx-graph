package com.gempukku.libgdx.graph.plugin.sprites.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteData;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteShaderLoaderCallback;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpriteShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.plugin.sprites.strategy.*;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.util.function.Function;

public class SpriteShaderRendererPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    private static final GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new SpriteShaderConfiguration()};
    private final PluginPrivateDataSource pluginPrivateDataSource;

    public SpriteShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new SpriteShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        final ShaderContextImpl shaderContext = new ShaderContextImpl(pluginPrivateDataSource);

        final ObjectMap<String, SpriteGraphShader> shaders = new ObjectMap<>();
        final Array<String> shaderTags = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final SpriteRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final RenderingStrategyCallback strategyCallback = new RenderingStrategyCallback(shaderContext,
                new Function<String, SpriteGraphShader>() {
                    @Override
                    public SpriteGraphShader apply(String s) {
                        return shaders.get(s);
                    }
                });

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> output = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", output);

        return new SingleInputsPipelineNode(result) {
            private FullScreenRender fullScreenRender;
            private TimeProvider timeProvider;
            private GraphSpritesImpl sprites;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                timeProvider = pipelineDataProvider.getTimeProvider();
                fullScreenRender = pipelineDataProvider.getFullScreenRender();
                sprites = pipelineDataProvider.getPrivatePluginData(GraphSpritesImpl.class);

                for (JsonValue shaderDefinition : shaderDefinitions) {
                    SpriteGraphShader shader = createColorShader(shaderDefinition, pipelineDataProvider.getWhitePixel().texture);
                    shaders.put(shader.getTag(), shader);
                    shaderTags.add(shader.getTag());
                }

                for (SpriteGraphShader shader : shaders.values()) {
                    sprites.registerTag(shader.getTag(), shader, renderingStrategy.isBatched());
                }
            }

            private boolean needsDepth() {
                for (SpriteGraphShader shader : shaders.values()) {
                    if (shader.isUsingDepthTexture() && sprites.hasSpriteWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            private boolean isRequiringSceneColor() {
                for (SpriteGraphShader shader : shaders.values()) {
                    if (shader.isUsingColorTexture() && sprites.hasSpriteWithTag(shader.getTag()))
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
                    Camera camera = cameraInput.getValue();

                    shaderContext.setCamera(camera);
                    shaderContext.setTimeProvider(timeProvider);
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    if (usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                        shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                    }

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    strategyCallback.prepare(pipelineRenderingContext, sprites);
                    currentBuffer.beginColor();
                    renderingStrategy.processSprites(sprites, shaderTags, shaderContext.getCamera(), strategyCallback);
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
                for (SpriteGraphShader shader : shaders.values()) {
                    shader.dispose();
                }
            }
        };
    }

    private SpriteRenderingStrategy createRenderingStrategy(RenderOrder renderOrder) {
        switch (renderOrder) {
            case Shader_Unordered:
                return new ShaderUnorderedSpriteRenderingStrategy();
            case Shader_Back_To_Front:
                return new ShaderBackToFrontSpriteRenderingStrategy();
            case Shader_Front_To_Back:
                return new ShaderFrontToBackSpriteRenderingStrategy();
            case Back_To_Front:
                return new BackToFrontSpriteRenderingStrategy();
            case Front_To_Back:
                return new FrontToBackSpriteRenderingStrategy();
        }
        throw new IllegalStateException("Unrecognized RenderOrder: " + renderOrder.name());
    }

    private static SpriteGraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return GraphLoader.loadGraph(shaderGraph, new SpriteShaderLoaderCallback(tag, defaultTexture, configurations), PropertyLocation.Attribute);
    }

    private static class RenderingStrategyCallback implements SpriteRenderingStrategy.StrategyCallback {
        private final ShaderContextImpl shaderContext;
        private final Function<String, SpriteGraphShader> shaderResolver;

        private GraphSpritesImpl graphSprites;
        private PipelineRenderingContext context;
        private GraphShader runningShader = null;

        public RenderingStrategyCallback(ShaderContextImpl shaderContext, Function<String, SpriteGraphShader> shaderResolver) {
            this.shaderContext = shaderContext;
            this.shaderResolver = shaderResolver;
        }

        public void prepare(PipelineRenderingContext context, GraphSpritesImpl graphSprites) {
            this.context = context;
            this.graphSprites = graphSprites;
        }

        @Override
        public void begin() {

        }

        @Override
        public void process(SpriteData spriteData, String tag) {
            SpriteGraphShader shader = shaderResolver.apply(tag);
            if (runningShader != shader) {
                endCurrentShader();

                shaderContext.setGlobalPropertyContainer(graphSprites.getGlobalProperties(tag));
                shader.begin(shaderContext, context.getRenderContext());
                runningShader = shader;
            }
            shader.renderSprites(shaderContext, spriteData);
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

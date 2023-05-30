package com.gempukku.libgdx.graph.render.postprocess.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;

public class GammaCorrectionPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public GammaCorrectionPipelineNodeProducer() {
        super(new GammaCorrectionPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineRendererConfiguration configuration) {
        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> pipelineOutput = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", pipelineOutput);

        return new SingleInputsPipelineNode(result, configuration) {
            private ShaderProgram shaderProgram;
            private FullScreenRender fullScreenRender;

            @Override
            public void initializePipeline() {
                FileHandleResolver assetResolver = configuration.getAssetResolver();
                shaderProgram = new ShaderProgram(
                        assetResolver.resolve("shader/viewToScreenCoords.vert"),
                        assetResolver.resolve("shader/gamma.frag"));
                if (!shaderProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

                fullScreenRender = configuration.getPipelineHelper().getFullScreenRender();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                PipelineNode.FieldOutput<Float> gammaInput = (PipelineNode.FieldOutput<Float>) inputs.get("gamma");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                float gamma = gammaInput != null ? gammaInput.getValue() : 0f;
                if (enabled && gamma != 1) {
                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    RenderPipelineBuffer newBuffer = renderPipeline.getNewFrameBuffer(currentBuffer, Color.BLACK);

                    OpenGLContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    newBuffer.beginColor();

                    shaderProgram.bind();

                    shaderProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(currentBuffer.getColorBufferTexture()));
                    shaderProgram.setUniformf("u_gamma", gamma);

                    fullScreenRender.renderFullScreen(shaderProgram);

                    newBuffer.endColor();

                    renderPipeline.swapColorTextures(currentBuffer, newBuffer);
                    renderPipeline.returnFrameBuffer(newBuffer);
                }

                pipelineOutput.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                shaderProgram.dispose();
            }
        };
    }
}

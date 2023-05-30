package com.gempukku.libgdx.graph.render.postprocess.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
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

public class GaussianBlurPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public GaussianBlurPipelineNodeProducer() {
        super(new GaussianBlurPipelineNodeConfiguration());
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
                        assetResolver.resolve("shader/gaussianBlur.frag"));
                if (!shaderProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

                fullScreenRender = configuration.getPipelineHelper().getFullScreenRender();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                PipelineNode.FieldOutput<Float> blurRadiusInput = (PipelineNode.FieldOutput<Float>) inputs.get("blurRadius");
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();
                int blurRadius = MathUtils.round(blurRadiusInput != null ? blurRadiusInput.getValue() : 0f);
                if (enabled && blurRadius > 0) {
                    float[] kernel = GaussianBlurKernel.getKernel(blurRadius);
                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    OpenGLContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    shaderProgram.bind();
                    shaderProgram.setUniformi("u_blurRadius", blurRadius);
                    shaderProgram.setUniformf("u_pixelSize", 1f / currentBuffer.getWidth(), 1f / currentBuffer.getHeight());
                    shaderProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

                    shaderProgram.setUniformi("u_vertical", 1);
                    RenderPipelineBuffer tempBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, pipelineRenderingContext.getRenderContext(), fullScreenRender);
                    shaderProgram.setUniformi("u_vertical", 0);
                    RenderPipelineBuffer finalBuffer = executeBlur(shaderProgram, renderPipeline, tempBuffer, pipelineRenderingContext.getRenderContext(), fullScreenRender);

                    renderPipeline.returnFrameBuffer(tempBuffer);
                    renderPipeline.swapColorTextures(currentBuffer, finalBuffer);
                    renderPipeline.returnFrameBuffer(finalBuffer);
                }

                pipelineOutput.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                shaderProgram.dispose();
            }
        };
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram shaderProgram, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                             OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);
        resultBuffer.beginColor();

        shaderProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(sourceBuffer.getColorBufferTexture()));

        fullScreenRender.renderFullScreen(shaderProgram);

        resultBuffer.endColor();

        return resultBuffer;
    }
}

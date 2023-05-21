package com.gempukku.libgdx.graph.render.postprocess.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.shader.context.OpenGLContext;

public class BloomPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public BloomPipelineNodeProducer() {
        super(new BloomPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, final PipelineDataProvider pipelineDataProvider) {
        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> pipelineOutput = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", pipelineOutput);

        return new SingleInputsPipelineNode(result, pipelineDataProvider) {
            private FullScreenRender fullScreenRender;
            private ShaderProgram bloomSumProgram;
            private ShaderProgram gaussianBlurPassProgram;
            private ShaderProgram brightnessFilterPassProgram;

            @Override
            public void initializePipeline() {
                FileHandleResolver assetResolver = pipelineDataProvider.getAssetResolver();
                brightnessFilterPassProgram = new ShaderProgram(
                        assetResolver.resolve("shader/viewToScreenCoords.vert"),
                        assetResolver.resolve("shader/brightnessFilter.frag"));
                if (!brightnessFilterPassProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + brightnessFilterPassProgram.getLog());
                gaussianBlurPassProgram = new ShaderProgram(
                        assetResolver.resolve("shader/viewToScreenCoords.vert"),
                        assetResolver.resolve("shader/gaussianBlur.frag"));
                if (!gaussianBlurPassProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + gaussianBlurPassProgram.getLog());
                bloomSumProgram = new ShaderProgram(
                        assetResolver.resolve("shader/viewToScreenCoords.vert"),
                        assetResolver.resolve("shader/bloomSum.frag"));
                if (!bloomSumProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + bloomSumProgram.getLog());

                fullScreenRender = pipelineDataProvider.getFullScreenRender();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");
                PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                PipelineNode.FieldOutput<Float> minimalBrightness = (PipelineNode.FieldOutput<Float>) inputs.get("minimalBrightness");
                PipelineNode.FieldOutput<Float> bloomRadius = (PipelineNode.FieldOutput<Float>) inputs.get("bloomRadius");
                PipelineNode.FieldOutput<Float> bloomStrength = (PipelineNode.FieldOutput<Float>) inputs.get("bloomStrength");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();

                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                float bloomStrengthValue = bloomStrength != null ? bloomStrength.getValue() : 0f;
                int bloomRadiusValue = MathUtils.round(bloomRadius != null ? bloomRadius.getValue() : 1f);
                if (enabled && bloomStrengthValue > 0 && bloomRadiusValue > 0) {
                    float minimalBrightnessValue = minimalBrightness != null ? minimalBrightness.getValue() : 0.7f;

                    RenderPipelineBuffer originalBuffer = renderPipeline.getDefaultBuffer();

                    OpenGLContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    RenderPipelineBuffer brightnessFilterBuffer = runBrightnessPass(minimalBrightnessValue, renderPipeline, originalBuffer, brightnessFilterPassProgram, pipelineRenderingContext.getRenderContext(), fullScreenRender);

                    RenderPipelineBuffer gaussianBlur = applyGaussianBlur(bloomRadiusValue, renderPipeline,
                            brightnessFilterBuffer, gaussianBlurPassProgram,
                            pipelineRenderingContext.getRenderContext(), fullScreenRender);
                    renderPipeline.returnFrameBuffer(brightnessFilterBuffer);

                    RenderPipelineBuffer result = applyTheBloom(bloomStrengthValue, renderPipeline, originalBuffer, gaussianBlur, bloomSumProgram, pipelineRenderingContext.getRenderContext(), fullScreenRender);

                    renderPipeline.returnFrameBuffer(gaussianBlur);

                    renderPipeline.swapColorTextures(originalBuffer, result);
                    renderPipeline.returnFrameBuffer(result);
                }

                pipelineOutput.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                bloomSumProgram.dispose();
                gaussianBlurPassProgram.dispose();
                brightnessFilterPassProgram.dispose();
            }
        };
    }

    private RenderPipelineBuffer applyTheBloom(float bloomStrength, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer, RenderPipelineBuffer gaussianBlur,
                                               ShaderProgram bloomSumProgram, OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer result = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);

        result.beginColor();

        bloomSumProgram.bind();

        bloomSumProgram.setUniformi("u_brightnessTexture", renderContext.bindTexture(gaussianBlur.getColorBufferTexture()));
        bloomSumProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(sourceBuffer.getColorBufferTexture()));
        bloomSumProgram.setUniformf("u_bloomStrength", bloomStrength);

        fullScreenRender.renderFullScreen(bloomSumProgram);

        result.endColor();

        return result;
    }


    private RenderPipelineBuffer applyGaussianBlur(int bloomRadius, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                                   ShaderProgram blurProgram, OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        float[] kernel = GaussianBlurKernel.getKernel(MathUtils.round(bloomRadius));
        blurProgram.bind();
        blurProgram.setUniformi("u_blurRadius", bloomRadius);
        blurProgram.setUniformf("u_pixelSize", 1f / sourceBuffer.getWidth(), 1f / sourceBuffer.getHeight());
        blurProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

        blurProgram.setUniformi("u_vertical", 1);
        RenderPipelineBuffer tempBuffer = executeBlur(blurProgram, renderPipeline, sourceBuffer, renderContext, fullScreenRender);
        blurProgram.setUniformi("u_vertical", 0);
        RenderPipelineBuffer blurredBuffer = executeBlur(blurProgram, renderPipeline, tempBuffer, renderContext, fullScreenRender);
        renderPipeline.returnFrameBuffer(tempBuffer);

        return blurredBuffer;
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram blurProgram, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                             OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);
        resultBuffer.beginColor();

        blurProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(sourceBuffer.getColorBufferTexture()));

        fullScreenRender.renderFullScreen(blurProgram);

        resultBuffer.endColor();

        return resultBuffer;
    }

    private RenderPipelineBuffer runBrightnessPass(float minimalBrightnessValue, RenderPipeline renderPipeline, RenderPipelineBuffer currentBuffer, ShaderProgram brightnessFilterPassProgram,
                                                   OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer brightnessFilterBuffer = renderPipeline.getNewFrameBuffer(currentBuffer, Color.BLACK);

        brightnessFilterBuffer.beginColor();

        brightnessFilterPassProgram.bind();

        brightnessFilterPassProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(currentBuffer.getColorBufferTexture()));
        brightnessFilterPassProgram.setUniformf("u_minimalBrightness", minimalBrightnessValue);

        fullScreenRender.renderFullScreen(brightnessFilterPassProgram);

        brightnessFilterBuffer.endColor();

        return brightnessFilterBuffer;
    }
}

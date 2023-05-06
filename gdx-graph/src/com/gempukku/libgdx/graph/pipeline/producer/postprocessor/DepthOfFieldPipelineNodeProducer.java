package com.gempukku.libgdx.graph.pipeline.producer.postprocessor;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.DepthOfFieldPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;

import java.nio.charset.StandardCharsets;

public class DepthOfFieldPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public DepthOfFieldPipelineNodeProducer() {
        super(new DepthOfFieldPipelineNodeConfiguration());
    }


    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        float maxBlurFloat = data.getFloat("maxBlur");
        final int maxBlur = MathUtils.round(maxBlurFloat);
        final boolean blurBackground = data.getBoolean("blurBackground", false);

        final ObjectMap<String, PipelineNode.FieldOutput<?>> result = new ObjectMap<>();
        final DefaultFieldOutput<RenderPipeline> pipelineOutput = new DefaultFieldOutput<>(PipelineFieldType.RenderPipeline);
        result.put("output", pipelineOutput);

        return new SingleInputsPipelineNode(result) {
            private ShaderProgram shaderProgram;
            private FullScreenRender fullScreenRender;

            @Override
            public void initializePipeline(PipelineDataProvider pipelineDataProvider) {
                FileHandleResolver assetResolver = pipelineDataProvider.getAssetResolver();
                String viewToScreenCoords = getShader(assetResolver, "viewToScreenCoords.vert");
                String depthOfField = getShader(assetResolver, "depthOfField.frag");
                depthOfField = depthOfField.replaceAll("MAX_BLUR", String.valueOf(maxBlur));
                depthOfField = depthOfField.replaceAll("UNPACK_FUNCTION;", GLSLFragmentReader.getFragment(assetResolver, "unpackVec3ToFloat"));
                depthOfField = depthOfField.replaceAll("BLUR_BACKGROUND", String.valueOf(blurBackground));

                shaderProgram = new ShaderProgram(
                        viewToScreenCoords, depthOfField);
                if (!shaderProgram.isCompiled())
                    throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

                fullScreenRender = pipelineDataProvider.getFullScreenRender();
            }

            private String getShader(FileHandleResolver assetResolver, String shaderName) {
                FileHandle fileHandle = assetResolver.resolve("shader/" + shaderName);
                return new String(fileHandle.readBytes(), StandardCharsets.UTF_8);
            }

            @Override
            public void processPipelineRequirements(PipelineRequirements pipelineRequirements) {
                if (maxBlur > 0)
                    pipelineRequirements.setRequiringDepthTexture();
            }

            @Override
            public void executeNode(PipelineRenderingContext pipelineRenderingContext, PipelineRequirementsCallback pipelineRequirementsCallback) {
                final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputs.get("input");
                final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputs.get("enabled");
                final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputs.get("camera");
                final PipelineNode.FieldOutput<Vector2> focusDistanceInput = (PipelineNode.FieldOutput<Vector2>) inputs.get("focusDistance");
                final PipelineNode.FieldOutput<Float> nearDistanceBlurInput = (PipelineNode.FieldOutput<Float>) inputs.get("nearDistanceBlur");
                final PipelineNode.FieldOutput<Float> farDistanceBlurInput = (PipelineNode.FieldOutput<Float>) inputs.get("farDistanceBlur");

                RenderPipeline renderPipeline = renderPipelineInput.getValue();
                boolean enabled = processorEnabled == null || processorEnabled.getValue();

                if (enabled && maxBlur > 0) {
                    Camera camera = cameraInput.getValue();
                    Vector2 focusDistance = focusDistanceInput.getValue();
                    float nearDistanceBlur = nearDistanceBlurInput != null ? nearDistanceBlurInput.getValue() : 10f;
                    float farDistanceBlur = farDistanceBlurInput != null ? farDistanceBlurInput.getValue() : 10f;

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    OpenGLContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    shaderProgram.bind();
                    shaderProgram.setUniformf("u_pixelSize", 1f / currentBuffer.getWidth(), 1f / currentBuffer.getHeight());
                    shaderProgram.setUniformf("u_cameraClipping", camera.near, camera.far);
                    shaderProgram.setUniformf("u_focusDistance", focusDistance);
                    shaderProgram.setUniformf("u_nearDistanceBlur", nearDistanceBlur);
                    shaderProgram.setUniformf("u_farDistanceBlur", farDistanceBlur);

                    shaderProgram.setUniformi("u_vertical", 1);
                    RenderPipelineBuffer tempBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, currentBuffer, pipelineRenderingContext.getRenderContext(), fullScreenRender);
                    shaderProgram.setUniformi("u_vertical", 0);
                    RenderPipelineBuffer finalBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, tempBuffer, pipelineRenderingContext.getRenderContext(), fullScreenRender);
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

    private RenderPipelineBuffer executeBlur(ShaderProgram shaderProgram, RenderPipeline renderPipeline, RenderPipelineBuffer depthBuffer, RenderPipelineBuffer sourceBuffer,
                                             OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);
        resultBuffer.beginColor();

        shaderProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(sourceBuffer.getColorBufferTexture()));
        shaderProgram.setUniformi("u_depthTexture", renderContext.bindTexture(depthBuffer.getDepthBufferTexture()));

        fullScreenRender.renderFullScreen(shaderProgram);

        resultBuffer.endColor();

        return resultBuffer;
    }
}

package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public class RenderToTextureOutput implements RenderOutput {
    private Texture texture;

    public RenderToTextureOutput(Texture texture) {
        this.texture = texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public int getRenderWidth() {
        return texture.getWidth();
    }

    @Override
    public int getRenderHeight() {
        return texture.getHeight();
    }

    @Override
    public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

        // Setup a buffer to draw on, with replaced texture
        RenderPipelineBufferImpl tmpBuffer = (RenderPipelineBufferImpl) renderPipeline.getNewFrameBuffer(currentBuffer, Color.BLACK);
        Texture oldTexture = tmpBuffer.getColorBufferTexture();
        tmpBuffer.getColorBuffer().setColorTexture(texture);

        renderPipeline.drawTexture(currentBuffer, tmpBuffer, pipelineRenderingContext, fullScreenRender);

        // Return buffer to previous state and give it back to pipeline
        tmpBuffer.getColorBuffer().setColorTexture(oldTexture);
        renderPipeline.returnFrameBuffer(tmpBuffer);
    }
}

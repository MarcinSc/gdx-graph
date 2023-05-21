package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public class RenderPipelineImpl implements RenderPipeline {
    private final BufferCopyHelper bufferCopyHelper;
    private final TextureFrameBufferCache textureFrameBufferCache;
    private final RenderPipelineBufferImpl defaultBuffer = new RenderPipelineBufferImpl();

    public RenderPipelineImpl(BufferCopyHelper bufferCopyHelper, TextureFrameBufferCache textureFrameBufferCache) {
        this.bufferCopyHelper = bufferCopyHelper;
        this.textureFrameBufferCache = textureFrameBufferCache;
    }

    @Override
    public RenderPipelineBufferImpl initializeDefaultBuffer(int width, int height, Pixmap.Format format, Color bgColor) {
        defaultBuffer.setColorBuffer(textureFrameBufferCache.obtainFrameBuffer(width, height, format), bgColor);
        return defaultBuffer;
    }

    @Override
    public void destroyDefaultBuffer() {
        returnFrameBuffer(defaultBuffer);
    }

    @Override
    public void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) renderPipelineBuffer;
        if (buffer.getDepthBuffer() == null) {
            TextureFrameBuffer depthBuffer = textureFrameBufferCache.obtainFrameBuffer(
                    buffer.getWidth(), buffer.getHeight(),
                    renderPipelineBuffer.getColorBufferTexture().getTextureData().getFormat());
            buffer.setDepthBuffer(depthBuffer, Color.WHITE);
        }
    }

    @Override
    public RenderPipelineBufferImpl getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom, Color bgColor) {
        RenderPipelineBufferImpl source = (RenderPipelineBufferImpl) takeSettingsFrom;
        return getNewFrameBuffer(source.getWidth(), source.getHeight(),
                takeSettingsFrom.getColorBufferTexture().getTextureData().getFormat(), bgColor);
    }

    @Override
    public RenderPipelineBufferImpl getNewFrameBuffer(int width, int height, Pixmap.Format format, Color bgColor) {
        TextureFrameBuffer frameBuffer = textureFrameBufferCache.obtainFrameBuffer(width, height, format);
        RenderPipelineBufferImpl renderPipelineBuffer = new RenderPipelineBufferImpl();
        renderPipelineBuffer.setColorBuffer(frameBuffer, bgColor);
        return renderPipelineBuffer;
    }

    @Override
    public void returnFrameBuffer(RenderPipelineBuffer frameBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) frameBuffer;
        textureFrameBufferCache.freeFrameBuffer(buffer.getColorBuffer());
        TextureFrameBuffer depthBuffer = buffer.getDepthBuffer();
        if (depthBuffer != null) {
            textureFrameBufferCache.freeFrameBuffer(depthBuffer);
            buffer.setDepthBuffer(null, null);
        }
    }

    @Override
    public void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender) {
        RenderPipelineBufferImpl fromBuffer = (RenderPipelineBufferImpl) paint;
        RenderPipelineBufferImpl toBuffer = (RenderPipelineBufferImpl) canvas;
        bufferCopyHelper.copy(fromBuffer.getColorBuffer(), toBuffer != null ? toBuffer.getColorBuffer() : null, pipelineRenderingContext.getRenderContext(), fullScreenRender);
    }

    @Override
    public void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender, float x, float y, float width, float height) {
        RenderPipelineBufferImpl fromBuffer = (RenderPipelineBufferImpl) paint;
        RenderPipelineBufferImpl toBuffer = (RenderPipelineBufferImpl) canvas;
        bufferCopyHelper.copy(fromBuffer.getColorBuffer(), toBuffer != null ? toBuffer.getColorBuffer() : null,
                pipelineRenderingContext.getRenderContext(), fullScreenRender, x, y, width, height);
    }

    @Override
    public void swapColorTextures(RenderPipelineBuffer buffer1, RenderPipelineBuffer buffer2) {
        RenderPipelineBufferImpl first = (RenderPipelineBufferImpl) buffer1;
        RenderPipelineBufferImpl second = (RenderPipelineBufferImpl) buffer2;
        Texture oldTexture = first.getColorBuffer().setColorTexture(second.getColorBufferTexture());
        second.getColorBuffer().setColorTexture(oldTexture);
    }

    public RenderPipelineBuffer getDefaultBuffer() {
        return defaultBuffer;
    }
}

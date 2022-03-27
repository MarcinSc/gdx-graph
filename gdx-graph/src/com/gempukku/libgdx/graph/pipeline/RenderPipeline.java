package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public interface RenderPipeline {
    RenderPipelineBuffer initializeDefaultBuffer(int width, int height, Pixmap.Format format, Color bgColor);

    RenderPipelineBuffer getDefaultBuffer();

    void destroyDefaultBuffer();

    void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer);

    RenderPipelineBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format, Color bgColor);

    RenderPipelineBuffer getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom, Color bgColor);

    void returnFrameBuffer(RenderPipelineBuffer frameBuffer);

    void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext,
                     FullScreenRender fullScreenRender);

    void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext,
                     FullScreenRender fullScreenRender,
                     float x, float y, float width, float height);

    void swapColorTextures(RenderPipelineBuffer buffer1, RenderPipelineBuffer buffer2);
}

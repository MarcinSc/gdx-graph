package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Pixmap;

public interface TextureFrameBufferCache {
    TextureFrameBuffer obtainFrameBuffer(int width, int height, Pixmap.Format format);

    void freeFrameBuffer(TextureFrameBuffer buffer);
}

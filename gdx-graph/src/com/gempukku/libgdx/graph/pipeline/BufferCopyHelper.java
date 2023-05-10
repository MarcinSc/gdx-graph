package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;

public interface BufferCopyHelper {
    void copy(FrameBuffer from, FrameBuffer to, OpenGLContext renderContext, FullScreenRender fullScreenRender);

    void copy(FrameBuffer from, FrameBuffer to, OpenGLContext renderContext, FullScreenRender fullScreenRender, float x, float y, float width, float height);
}

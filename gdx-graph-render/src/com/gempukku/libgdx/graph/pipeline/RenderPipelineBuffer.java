package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Texture;

public interface RenderPipelineBuffer {
    Texture getDepthBufferTexture();

    Texture getColorBufferTexture();

    void beginColor();

    void endColor();

    void beginDepth();

    void endDepth();

    int getWidth();

    int getHeight();
}

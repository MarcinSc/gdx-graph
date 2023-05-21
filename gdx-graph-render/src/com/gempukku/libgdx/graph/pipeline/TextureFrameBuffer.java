package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class TextureFrameBuffer extends FrameBuffer {
    public TextureFrameBuffer(int width, int height, Pixmap.Format format) {
        super(format, width, height, true, false);
    }

    public Texture setColorTexture(Texture texture) {
        begin();
        attachFrameBufferColorTexture(texture);
        end();

        Texture oldTexture = textureAttachments.removeIndex(0);
        textureAttachments.insert(0, texture);
        return oldTexture;
    }
}

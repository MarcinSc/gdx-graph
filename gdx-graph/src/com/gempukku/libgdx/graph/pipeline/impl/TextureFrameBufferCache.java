package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBuffer;

import java.util.Iterator;

public class TextureFrameBufferCache implements Disposable {
    private final Array<TextureFrameBuffer> oldFrameBuffers = new Array<>();
    private final Array<TextureFrameBuffer> newFrameBuffers = new Array<>();

    public void startFrame() {

    }

    public void endFrame() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        oldFrameBuffers.addAll(newFrameBuffers);
        newFrameBuffers.clear();
    }

    @Override
    public void dispose() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        for (FrameBuffer freeFrameBuffer : newFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        newFrameBuffers.clear();
    }

    public TextureFrameBuffer getOrCreateFrameBuffer(int width, int height, Pixmap.Format format) {
        TextureFrameBuffer buffer = extractFrameBuffer(width, height, this.newFrameBuffers);
        if (buffer != null)
            return buffer;
        buffer = extractFrameBuffer(width, height, this.oldFrameBuffers);
        if (buffer != null)
            return buffer;

        return new TextureFrameBuffer(width, height, format);
    }

    public void returnBuffer(TextureFrameBuffer buffer) {
        newFrameBuffers.add(buffer);
    }

    private TextureFrameBuffer extractFrameBuffer(int width, int height, Array<TextureFrameBuffer> frameBuffers) {
        Iterator<TextureFrameBuffer> iterator = frameBuffers.iterator();
        while (iterator.hasNext()) {
            TextureFrameBuffer buffer = iterator.next();
            if (buffer.getWidth() == width && buffer.getHeight() == height) {
                iterator.remove();
                return buffer;
            }
        }
        return null;
    }
}

package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class RenderPipelineBufferImpl implements RenderPipelineBuffer {
    private TextureFrameBuffer colorBuffer;
    private Color colorBgColor;
    private TextureFrameBuffer depthBuffer;
    private Color depthBgColor;

    public TextureFrameBuffer getDepthBuffer() {
        return depthBuffer;
    }

    public void setDepthBuffer(TextureFrameBuffer depthBuffer, Color depthBgColor) {
        this.depthBuffer = depthBuffer;
        this.depthBgColor = depthBgColor;
    }

    public Texture getDepthBufferTexture() {
        return depthBuffer.getColorBufferTexture();
    }

    public TextureFrameBuffer getColorBuffer() {
        return colorBuffer;
    }

    public void setColorBuffer(TextureFrameBuffer colorBuffer, Color colorBgColor) {
        this.colorBuffer = colorBuffer;
        this.colorBgColor = colorBgColor;
    }

    public Texture getColorBufferTexture() {
        return colorBuffer.getColorBufferTexture();
    }

    public void beginColor() {
        colorBuffer.begin();
        if (colorBgColor != null) {
            Gdx.gl.glClearColor(colorBgColor.r, colorBgColor.g, colorBgColor.b, colorBgColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            colorBgColor = null;
        }
    }

    public void endColor() {
        colorBuffer.end();
    }

    public void beginDepth() {
        depthBuffer.begin();
        if (depthBgColor != null) {
            Gdx.gl.glClearColor(depthBgColor.r, depthBgColor.g, depthBgColor.b, depthBgColor.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            depthBgColor = null;
        }
    }

    public void endDepth() {
        depthBuffer.end();
    }

    public int getWidth() {
        return colorBuffer.getWidth();
    }

    public int getHeight() {
        return colorBuffer.getHeight();
    }
}

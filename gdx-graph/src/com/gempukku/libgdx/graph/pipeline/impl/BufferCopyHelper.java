package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;

public class BufferCopyHelper {
    private final ShaderProgram shaderProgram;

    public BufferCopyHelper() {
        shaderProgram = new ShaderProgram(
                Gdx.files.classpath("shader/draw/drawTexture.vert"),
                Gdx.files.classpath("shader/draw/drawTexture.frag")
        );
    }

    public void copy(FrameBuffer from, FrameBuffer to, OpenGLContext renderContext, FullScreenRender fullScreenRender) {
        copy(from, to, renderContext, fullScreenRender, 0, 0, getBufferWidth(to), getBufferHeight(to));
    }

    public void copy(FrameBuffer from, FrameBuffer to, OpenGLContext renderContext, FullScreenRender fullScreenRender, float x, float y, float width, float height) {
        if (to != null) {
            to.begin();
        }

        float bufferWidth = getBufferWidth(to);
        float bufferHeight = getBufferHeight(to);

        renderContext.setDepthTest(0);
        renderContext.setDepthMask(false);
        renderContext.setBlending(false, 0, 0);
        renderContext.setCullFace(GL20.GL_BACK);

        shaderProgram.bind();
        shaderProgram.setUniformi("u_sourceTexture", renderContext.bindTexture(from.getColorBufferTexture()));
        shaderProgram.setUniformf("u_sourcePosition", 0, 0);
        shaderProgram.setUniformf("u_sourceSize", 1, 1);
        shaderProgram.setUniformf("u_targetPosition", x / bufferWidth, y / bufferHeight);
        shaderProgram.setUniformf("u_targetSize", width / bufferWidth, height / bufferHeight);

        fullScreenRender.renderFullScreen(shaderProgram);
        shaderProgram.end();

        if (to != null) {
            to.end();
        }
    }

    public void dispose() {
        shaderProgram.dispose();
    }

    private int getBufferWidth(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getWidth();
        return to.getWidth();
    }

    private int getBufferHeight(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getHeight();
        return to.getHeight();
    }
}

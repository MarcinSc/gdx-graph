package com.gempukku.libgdx.graph.libgdx.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.IntBuffer;

public class DirectOpenGLContext implements OpenGLContext {
    private final static int MAX_GLES_UNITS = 32;
    /**
     * The index of the first exclusive texture unit
     */
    private final static int MAX_TEXTURE_UNITS = Math.min(getMaxTextureUnits(), MAX_GLES_UNITS);

    private static int getMaxTextureUnits() {
        IntBuffer buffer = BufferUtils.newIntBuffer(16);
        Gdx.gl.glGetIntegerv(GL20.GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
        return buffer.get(0);
    }

    private int nextTextureUnit = 0;

    @Override
    public void begin() {
        nextTextureUnit = 0;
    }

    @Override
    public void end() {

    }

    @Override
    public void setDepthMask(boolean depthMask) {
        Gdx.gl.glDepthMask(depthMask);
    }

    @Override
    public void setDepthTest(int depthFunction) {
        setDepthTest(depthFunction, 0f, 1f);
    }

    @Override
    public void setDepthTest(int depthFunction, float depthRangeNear, float depthRangeFar) {
        boolean enabled = depthFunction != 0;
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(depthFunction);
            Gdx.gl.glDepthRangef(depthRangeNear, depthRangeFar);
        } else {
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        }
    }

    @Override
    public void setBlending(boolean enabled, int sFactor, int dFactor) {
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(sFactor, dFactor);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void setBlendingSeparate(boolean enabled, int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha) {
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFuncSeparate(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void setCullFace(int face) {
        boolean enabled = face != 0;
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_CULL_FACE);
            Gdx.gl.glCullFace(face);
        } else {
            Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        }
    }

    @Override
    public int bindTexture(TextureDescriptor<?> textureDescriptor) {
        int textureUnit = getNextTextureUnit();
        GLTexture texture = textureDescriptor.texture;
        texture.bind(textureUnit);
        texture.unsafeSetWrap(textureDescriptor.uWrap, textureDescriptor.vWrap);
        texture.unsafeSetFilter(textureDescriptor.minFilter, textureDescriptor.magFilter);
        return textureUnit;
    }

    @Override
    public int bindTexture(GLTexture texture) {
        int textureUnit = getNextTextureUnit();
        texture.bind(textureUnit);
        return textureUnit;
    }

    private int getNextTextureUnit() {
        int result = nextTextureUnit;
        nextTextureUnit++;
        if (nextTextureUnit == MAX_TEXTURE_UNITS)
            nextTextureUnit = 0;
        return result;
    }
}

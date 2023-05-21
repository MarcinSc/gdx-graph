package com.gempukku.libgdx.graph.pipeline.shader.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.TextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;

public class StateOpenGLContext implements OpenGLContext {
    private final TextureBinder textureBinder;
    private boolean blending;
    private int blendSFactor;
    private int blendDFactor;
    private int blendSFactorAlpha;
    private int blendDFactorAlpha;
    private int depthFunc;
    private float depthRangeNear;
    private float depthRangeFar;
    private boolean depthMask;
    private int cullFace;

    public StateOpenGLContext() {
        this.textureBinder = new DefaultTextureBinder(DefaultTextureBinder.LRU, 1);
    }

    /**
     * Sets up the render context, must be matched with a call to {@link #end()}.
     */
    @Override
    public void begin() {
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        depthFunc = 0;
        Gdx.gl.glDepthMask(true);
        depthMask = true;
        Gdx.gl.glDisable(GL20.GL_BLEND);
        blending = false;
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        cullFace = blendSFactor = blendDFactor = blendSFactorAlpha = blendDFactorAlpha = 0;
        textureBinder.begin();
    }

    /**
     * Resets all changed OpenGL states to their defaults.
     */
    @Override
    public void end() {
        if (depthFunc != 0) Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        if (!depthMask) Gdx.gl.glDepthMask(true);
        if (blending) Gdx.gl.glDisable(GL20.GL_BLEND);
        if (cullFace > 0) Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        textureBinder.end();
    }

    @Override
    public void setDepthMask(final boolean depthMask) {
        if (this.depthMask != depthMask) Gdx.gl.glDepthMask(this.depthMask = depthMask);
    }

    @Override
    public void setDepthTest(final int depthFunction) {
        setDepthTest(depthFunction, 0f, 1f);
    }

    @Override
    public void setDepthTest(final int depthFunction, final float depthRangeNear, final float depthRangeFar) {
        final boolean wasEnabled = depthFunc != 0;
        final boolean enabled = depthFunction != 0;
        if (depthFunc != depthFunction) {
            depthFunc = depthFunction;
            if (enabled) {
                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(depthFunction);
            } else
                Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        }
        if (enabled) {
            if (!wasEnabled || depthFunc != depthFunction) Gdx.gl.glDepthFunc(depthFunc = depthFunction);
            if (!wasEnabled || this.depthRangeNear != depthRangeNear || this.depthRangeFar != depthRangeFar)
                Gdx.gl.glDepthRangef(this.depthRangeNear = depthRangeNear, this.depthRangeFar = depthRangeFar);
        }
    }

    @Override
    public void setBlending(final boolean enabled, final int sFactor, final int dFactor) {
        setBlendingSeparate(enabled, sFactor, dFactor, sFactor, dFactor);
    }

    @Override
    public void setBlendingSeparate(boolean enabled, int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha) {
        if (enabled != blending) {
            blending = enabled;
            if (enabled)
                Gdx.gl.glEnable(GL20.GL_BLEND);
            else
                Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        if (enabled && (blendSFactor != sFactor || blendDFactor != dFactor
                || blendSFactorAlpha != sFactorAlpha || blendDFactorAlpha != dFactorAlpha)) {
            if (sFactor == sFactorAlpha && dFactor == dFactorAlpha)
                Gdx.gl.glBlendFunc(sFactor, dFactor);
            else
                Gdx.gl.glBlendFuncSeparate(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
            blendSFactor = sFactor;
            blendDFactor = dFactor;
            blendSFactorAlpha = sFactorAlpha;
            blendDFactorAlpha = dFactorAlpha;
        }
    }

    @Override
    public void setCullFace(final int face) {
        if (face != cullFace) {
            cullFace = face;
            if ((face == GL20.GL_FRONT) || (face == GL20.GL_BACK) || (face == GL20.GL_FRONT_AND_BACK)) {
                Gdx.gl.glEnable(GL20.GL_CULL_FACE);
                Gdx.gl.glCullFace(face);
            } else
                Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        }
    }

    @Override
    public int bindTexture(TextureDescriptor<?> textureDescriptor) {
        return textureBinder.bind(textureDescriptor);
    }

    @Override
    public int bindTexture(GLTexture texture) {
        return textureBinder.bind(texture);
    }
}

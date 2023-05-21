package com.gempukku.libgdx.graph.pipeline.shader.context;

import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;

public interface OpenGLContext {
    void begin();

    void end();

    void setDepthMask(boolean depthMask);

    void setDepthTest(int depthFunction);

    void setDepthTest(int depthFunction, float depthRangeNear, float depthRangeFar);

    void setBlending(boolean enabled, int sFactor, int dFactor);

    void setBlendingSeparate(boolean enabled, int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha);

    void setCullFace(int face);

    int bindTexture(TextureDescriptor<?> textureDescriptor);

    int bindTexture(GLTexture texture);
}

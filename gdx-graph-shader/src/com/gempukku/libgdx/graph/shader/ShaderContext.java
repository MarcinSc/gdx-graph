package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public interface ShaderContext {
    int getRenderWidth();

    int getRenderHeight();

    GraphShader getGraphShader();

    ShaderRendererConfiguration getShaderRenderingConfiguration();

    Object getModel();

    Texture getDepthTexture();

    Texture getColorTexture();

    TextureRegion getDefaultTexture();

    Camera getCamera();

    TimeProvider getTimeProvider();

    Object getGlobalProperty(String name);

    Object getLocalProperty(String name);
}

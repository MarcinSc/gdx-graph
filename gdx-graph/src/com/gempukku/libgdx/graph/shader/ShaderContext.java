package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface ShaderContext extends PluginPrivateDataSource {
    int getRenderWidth();

    int getRenderHeight();

    Texture getDepthTexture();

    Texture getColorTexture();

    Camera getCamera();

    TimeProvider getTimeProvider();

    Object getGlobalProperty(String name);

    Object getLocalProperty(String name);
}

package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface ShaderContext extends PluginPrivateDataSource {
    int getRenderWidth();

    int getRenderHeight();

    RenderableModel getRenderableModel();

    Texture getDepthTexture();

    Texture getColorTexture();

    TextureRegion getDefaultTexture();

    Camera getCamera();

    TimeProvider getTimeProvider();

    Object getGlobalProperty(String name);

    Object getLocalProperty(String name);
}

package com.gempukku.libgdx.graph.pipeline.producer.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class ShaderContextImpl implements ShaderContext {
    private int renderWidth;
    private int renderHeight;

    private Camera camera;
    private Texture depthTexture;
    private Texture colorTexture;
    private TimeProvider timeProvider;
    private PropertyContainer globalPropertyContainer;
    private PropertyContainer localPropertyContainer;

    private final PluginPrivateDataSource pluginPrivateDataSource;

    public ShaderContextImpl(PluginPrivateDataSource pluginPrivateDataSource) {
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public int getRenderWidth() {
        return renderWidth;
    }

    public void setRenderWidth(int renderWidth) {
        this.renderWidth = renderWidth;
    }

    @Override
    public int getRenderHeight() {
        return renderHeight;
    }

    public void setRenderHeight(int renderHeight) {
        this.renderHeight = renderHeight;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Texture getDepthTexture() {
        return depthTexture;
    }

    public void setDepthTexture(Texture depthTexture) {
        this.depthTexture = depthTexture;
    }

    @Override
    public Texture getColorTexture() {
        return colorTexture;
    }

    public void setColorTexture(Texture colorTexture) {
        this.colorTexture = colorTexture;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void setGlobalPropertyContainer(PropertyContainer globalPropertyContainer) {
        this.globalPropertyContainer = globalPropertyContainer;
    }

    public void setLocalPropertyContainer(PropertyContainer localPropertyContainer) {
        this.localPropertyContainer = localPropertyContainer;
    }

    @Override
    public Object getGlobalProperty(String name) {
        return globalPropertyContainer.getValue(name);
    }

    @Override
    public Object getLocalProperty(String name) {
        return localPropertyContainer.getValue(name);
    }

    @Override
    public <T> T getPrivatePluginData(Class<T> clazz) {
        return pluginPrivateDataSource.getPrivatePluginData(clazz);
    }
}

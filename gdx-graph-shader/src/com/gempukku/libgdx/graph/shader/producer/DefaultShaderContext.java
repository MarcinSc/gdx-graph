package com.gempukku.libgdx.graph.shader.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

public class DefaultShaderContext implements ShaderContext {
    private PipelineRendererConfiguration pipelineRendererConfiguration;

    private int renderWidth;
    private int renderHeight;
    private Object model;

    private Camera camera;
    private Texture depthTexture;
    private Texture colorTexture;
    private TimeProvider timeProvider;
    private PropertyContainer globalPropertyContainer;
    private PropertyContainer localPropertyContainer;
    private GraphShader graphShader;

    @Override
    public GraphShader getGraphShader() {
        return graphShader;
    }

    public void setGraphShader(GraphShader graphShader) {
        this.graphShader = graphShader;
    }

    @Override
    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public void setPipelineRendererConfiguration(PipelineRendererConfiguration pipelineRendererConfiguration) {
        this.pipelineRendererConfiguration = pipelineRendererConfiguration;
    }

    @Override
    public ShaderRendererConfiguration getShaderRenderingConfiguration() {
        return pipelineRendererConfiguration.getConfig(ShaderRendererConfiguration.class);
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
    public TextureRegion getDefaultTexture() {
        return pipelineRendererConfiguration.getPipelineHelper().getWhitePixel().textureRegion;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return pipelineRendererConfiguration.getTimeProvider();
    }

    public void setGlobalPropertyContainer(PropertyContainer globalPropertyContainer) {
        this.globalPropertyContainer = globalPropertyContainer;
    }

    public void setLocalPropertyContainer(PropertyContainer localPropertyContainer) {
        this.localPropertyContainer = localPropertyContainer;
    }

    private PropertyContainer getPipelinePropertyContainer() {
        return pipelineRendererConfiguration.getPipelinePropertyContainer();
    }

    @Override
    public Object getGlobalProperty(String name) {
        Object value = globalPropertyContainer.getValue(name);
        if (value != null)
            return value;
        return getPipelinePropertyContainer().getValue(name);
    }

    @Override
    public Object getLocalProperty(String name) {
        Object value = localPropertyContainer.getValue(name);
        if (value != null)
            return value;
        value = globalPropertyContainer.getValue(name);
        if (value != null)
            return value;
        return getPipelinePropertyContainer().getValue(name);
    }
}

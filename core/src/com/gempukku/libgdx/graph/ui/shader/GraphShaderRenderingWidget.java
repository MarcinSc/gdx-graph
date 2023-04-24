package com.gempukku.libgdx.graph.ui.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.libgdx.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.DefaultShaderContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.ui.DisposableWidget;

public class GraphShaderRenderingWidget extends DisposableWidget {
    private final DefaultShaderContext shaderContext;
    private final StateOpenGLContext renderContext;

    private boolean initialized;
    private FrameBuffer frameBuffer;

    private Camera camera;
    private GraphShader graphShader;
    private RenderableModel renderableModel;

    public GraphShaderRenderingWidget(PluginPrivateDataSource pluginPrivateDataSource) {
        shaderContext = new DefaultShaderContext(pluginPrivateDataSource);
        renderContext = new StateOpenGLContext();
    }

    public void setColorTexture(Texture texture) {
        shaderContext.setColorTexture(texture);
    }

    public void setDepthTexture(Texture texture) {
        shaderContext.setDepthTexture(texture);
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        shaderContext.setTimeProvider(timeProvider);
    }

    public void setGlobalPropertyContainer(PropertyContainer propertyContainer) {
        shaderContext.setGlobalPropertyContainer(propertyContainer);
    }

    public void setLocalPropertyContainer(PropertyContainer propertyContainer) {
        shaderContext.setLocalPropertyContainer(propertyContainer);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        shaderContext.setCamera(camera);
    }

    public void setGraphShader(GraphShader graphShader) {
        this.graphShader = graphShader;
    }

    public void setRenderableModel(RenderableModel renderableModel) {
        this.renderableModel = renderableModel;
        shaderContext.setRenderableModel(renderableModel);
    }

    @Override
    protected void initializeWidget() {
        initialized = true;
    }

    @Override
    public void act(float delta) {
        validate();
        super.act(delta);
        int width = MathUtils.round(getWidth());
        int height = MathUtils.round(getHeight());
        if (initialized && width > 0 && height > 0) {
            if (hasToRecreateBuffer(width, height)) {
                if (frameBuffer != null) {
                    frameBuffer.dispose();
                }
                frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
                shaderContext.setRenderWidth(width);
                shaderContext.setRenderHeight(height);
            }
            drawToOffscreen(width, height);
        }
    }

    private boolean hasToRecreateBuffer(int width, int height) {
        return frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height;
    }

    private void drawToOffscreen(int width, int height) {
        frameBuffer.begin();
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        renderContext.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        graphShader.begin(shaderContext, renderContext);
        graphShader.render(shaderContext, renderableModel);
        graphShader.end();
        renderContext.end();
        frameBuffer.end();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (frameBuffer != null) {
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + getHeight(), getWidth(), -getHeight());
        }
    }

    @Override
    protected void disposeWidget() {
        initialized = false;
        if (frameBuffer != null) {
            frameBuffer.dispose();
            frameBuffer = null;
        }
    }
}

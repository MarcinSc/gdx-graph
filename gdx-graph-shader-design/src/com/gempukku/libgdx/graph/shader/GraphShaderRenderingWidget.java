package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.shader.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.shader.producer.DefaultShaderContext;
import com.gempukku.libgdx.ui.DisposableWidget;

public class GraphShaderRenderingWidget extends DisposableWidget {
    private final DefaultShaderContext shaderContext;
    private final StateOpenGLContext renderContext;
    private final MapWritablePropertyContainer rootPropertyContainer = new MapWritablePropertyContainer();
    private final ObjectMap<Class<?>, Object> privatePluginData = new ObjectMap<>();

    private boolean initialized;
    private FrameBuffer frameBuffer;

    private Camera camera;
    private GraphShader graphShader;
    private RenderableModel renderableModel;

    public GraphShaderRenderingWidget() {
        shaderContext = new DefaultShaderContext(rootPropertyContainer,
                new PluginPrivateDataSource() {
                    @Override
                    public <T> T getPrivatePluginData(Class<T> clazz) {
                        return (T) privatePluginData.get(clazz);
                    }
                }, WhitePixel.sharedInstance.textureRegion);
        renderContext = new StateOpenGLContext();
    }

    public <T> void addPrivatePluginData(Class<T> clazz, T data) {
        privatePluginData.put(clazz, data);
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
            shaderContext.setRenderWidth(width);
            shaderContext.setRenderHeight(height);

            if (hasToRecreateBuffer(width, height)) {
                if (frameBuffer != null) {
                    frameBuffer.dispose();
                }
                frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
            }

            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update();

            drawToOffscreen();
        }
    }

    private boolean hasToRecreateBuffer(int width, int height) {
        return frameBuffer == null || frameBuffer.getWidth() != width || frameBuffer.getHeight() != height;
    }

    private void drawToOffscreen() {
        frameBuffer.begin();
        renderContext.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
        if (graphShader != null && renderableModel != null && renderableModel.isRendered(graphShader, camera)) {
            shaderContext.setRenderableModel(renderableModel);
            graphShader.begin(shaderContext, renderContext);
            graphShader.render(shaderContext, renderableModel);
            graphShader.end();
        }
        renderContext.end();
        frameBuffer.end();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (initialized && renderableModel != null && graphShader != null && frameBuffer != null) {
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

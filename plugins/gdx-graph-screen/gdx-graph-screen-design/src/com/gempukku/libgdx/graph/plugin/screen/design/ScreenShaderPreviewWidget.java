package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.libgdx.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.plugin.screen.ScreenGraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ScreenShaderPreviewWidget extends Widget implements Disposable {
    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private ScreenGraphShader graphShader;
    private OpenGLContext renderContext;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private Lighting3DEnvironment graphShaderEnvironment;
    private ShaderContextImpl shaderContext;
    private FullScreenRenderImpl fullScreenRender;

    public ScreenShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new StateOpenGLContext();
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.77f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        graphShaderEnvironment = new Lighting3DEnvironment();
        graphShaderEnvironment.setAmbientColor(Color.DARK_GRAY);
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -2f, 1f, 1f, 2f);
        graphShaderEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderEnvironment);

        PluginPrivateDataSource dataSource = new PluginPrivateDataSource() {
            @Override
            public <T> T getPrivatePluginData(Class<T> clazz) {
                if (clazz == Lighting3DPrivateData.class)
                    return (T) data;
                return null;
            }
        };

        shaderContext = new ShaderContextImpl(dataSource);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);

        fullScreenRender = new FullScreenRenderImpl();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null && shaderInitialized) {
            destroyShader();
        }
    }

    @Override
    public float getPrefWidth() {
        return width;
    }

    @Override
    public float getPrefHeight() {
        return height;
    }

    private void createShader(final GraphWithProperties graph) {
        try {
            timeKeeper = new DefaultTimeKeeper();
            graphShader = GraphShaderBuilder.buildScreenShader("Test", WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

            shaderContext.setTimeProvider(timeKeeper);
            shaderContext.setGlobalPropertyContainer(
                    new PropertyContainer() {
                        @Override
                        public Object getValue(String name) {
                            for (GraphProperty property : graph.getProperties()) {
                                if (property.getName().equals(name)) {
                                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                                    Object value = propertyType.convertFromJson(property.getData());
                                    if (propertyType.isTexture()) {
                                        if (value != null) {
                                            try {
                                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                                graphShader.addManagedResource(texture);
                                                return new TextureRegion(texture);
                                            } catch (Exception exp) {

                                            }
                                        }
                                        return WhitePixel.sharedInstance.textureRegion;
                                    } else {
                                        return value;
                                    }
                                }
                            }

                            return null;
                        }
                    });

            shaderInitialized = true;
        } catch (Exception exp) {
            fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            if (graphShader != null)
                graphShader.dispose();
        }
    }

    private void destroyShader() {
        frameBuffer.dispose();
        frameBuffer = null;
        graphShader.dispose();
        shaderInitialized = false;
    }

    @Override
    public void dispose() {
        fullScreenRender.dispose();
        if (shaderInitialized)
            destroyShader();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (frameBuffer != null) {
            batch.end();

            timeKeeper.updateTime(Gdx.graphics.getDeltaTime());
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            try {
                frameBuffer.begin();
                camera.viewportWidth = width;
                camera.viewportHeight = height;
                camera.update();

                renderContext.begin();
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                graphShader.begin(shaderContext, renderContext);
                graphShader.render(shaderContext, fullScreenRender);
                graphShader.end();
                frameBuffer.end();
                renderContext.end();
            } catch (Exception exp) {
                // Ignore
                fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            } finally {
                if (ScissorStack.peekScissors() != null)
                    Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            }

            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + height, width, -height);
        }
    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        if (hasErrors && shaderInitialized) {
            destroyShader();
        } else if (!hasErrors && !shaderInitialized) {
            createShader(graph);
        } else if (!hasErrors && shaderInitialized) {
            destroyShader();
            createShader(graph);
        }
    }
}

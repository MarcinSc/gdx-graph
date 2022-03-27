package com.gempukku.libgdx.graph.plugin.sprites.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.libgdx.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpriteImpl;
import com.gempukku.libgdx.graph.plugin.sprites.impl.NonBatchedTagSpriteData;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class SpriteShaderPreviewWidget extends Widget implements Disposable {
    private final MapWritablePropertyContainer propertyContainer;
    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private SpriteGraphShader graphShader;
    private OpenGLContext renderContext;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private ShaderContextImpl shaderContext;
    private GraphSpriteImpl graphSprite;
    private NonBatchedTagSpriteData spriteData;

    public SpriteShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new StateOpenGLContext();
        camera = new OrthographicCamera(width, height);
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(0f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 1f);
        camera.update();

        PluginPrivateDataSource dataSource = new PluginPrivateDataSource() {
            @Override
            public <T> T getPrivatePluginData(Class<T> clazz) {
                return null;
            }
        };

        shaderContext = new ShaderContextImpl(dataSource);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);

        final Vector3 position = new Vector3(0, 0, 2);

        propertyContainer = new MapWritablePropertyContainer();

        graphSprite = new GraphSpriteImpl("Test",
                new RenderableSprite() {
                    @Override
                    public Vector3 getPosition() {
                        return position;
                    }

                    @Override
                    public boolean isRendered(Camera camera) {
                        return true;
                    }

                    @Override
                    public PropertyContainer getPropertyContainer(String tag) {
                        return propertyContainer;
                    }
                });
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

    private void createShader(final Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
        try {
            timeKeeper = new DefaultTimeKeeper();
            graphShader = GraphShaderBuilder.buildSpriteShader("Test", WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

            createModel(graphShader.getVertexAttributes(), graphShader.getProperties());
            shaderContext.setGlobalPropertyContainer(
                    new PropertyContainer() {
                        @Override
                        public Object getValue(String name) {
                            for (GraphProperty property : graph.getProperties()) {
                                if (property.getName().equals(name) && property.getLocation() == PropertyLocation.Global_Uniform) {
                                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                                    return propertyType.convertFromJson(property.getData());
                                }
                            }

                            return null;
                        }
                    });
            propertyContainer.clear();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() != PropertyLocation.Global_Uniform) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    Object value = propertyType.convertFromJson(property.getData());
                    if (propertyType.isTexture()) {
                        if (value != null) {
                            try {
                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                graphShader.addManagedResource(texture);
                                propertyContainer.setValue(property.getName(), new TextureRegion(texture));
                            } catch (Exception exp) {
                                propertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                            }
                        } else {
                            propertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                        }
                    } else {
                        propertyContainer.setValue(property.getName(), value);
                    }
                }
            }

            shaderContext.setTimeProvider(timeKeeper);

            shaderInitialized = true;
        } catch (Exception exp) {
            exp.printStackTrace();
            if (graphShader != null)
                graphShader.dispose();
        }
    }

    private Object getPropertyValue(final GraphProperty property) {
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

    private void createModel(VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> properties) {
        spriteData = new NonBatchedTagSpriteData("Test", vertexAttributes, properties);
        spriteData.setSprite(graphSprite);
    }

    private void destroyShader() {
        frameBuffer.dispose();
        frameBuffer = null;
        graphShader.dispose();
        shaderInitialized = false;
    }

    @Override
    public void dispose() {
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
                graphShader.renderSprites(shaderContext, spriteData);
                graphShader.end();
                frameBuffer.end();
                renderContext.end();
            } catch (Exception exp) {
                // Ignore
                exp.printStackTrace();
            } finally {
                if (ScissorStack.peekScissors() != null)
                    Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            }

            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + height, width, -height);
        }
    }

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
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

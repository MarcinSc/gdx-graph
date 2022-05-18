package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.libgdx.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ModelShaderPreviewWidget extends Widget implements Disposable {
    public enum ShaderPreviewModel {
        Sphere, Rectangle;
    }

    private Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph;
    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private GraphShader graphShader;
    private OpenGLContext renderContext;

    private MeshBasedRenderableModel rectangleShaderModel;
    private MeshBasedRenderableModel sphereShaderModel;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private Lighting3DEnvironment graphShaderEnvironment;
    private ShaderContextImpl shaderContext;
    private ShaderPreviewModel model = ShaderPreviewModel.Sphere;

    private final MapWritablePropertyContainer localPropertyContainer;

    public ModelShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new StateOpenGLContext();
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.9f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        graphShaderEnvironment = new Lighting3DEnvironment();
        graphShaderEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -4f, 1.8f, 1.8f, 8f);
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

        localPropertyContainer = new MapWritablePropertyContainer();

        createModelPropertyContainers();
    }

    private void createModelPropertyContainers() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();

        Model rectangleModel = modelBuilder.createRect(
                0, -0.5f, -0.5f,
                0, -0.5f, 0.5f,
                0, 0.5f, 0.5f,
                0, 0.5f, -0.5f,
                1, 0, 0,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        float sphereDiameter = 0.8f;
        Model sphereModel = modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);

        rectangleShaderModel = new MeshBasedRenderableModel(rectangleModel.meshes.get(0));
        sphereShaderModel = new MeshBasedRenderableModel(sphereModel.meshes.get(0));

        rectangleModel.dispose();
        sphereModel.dispose();
    }

    public void setModel(ShaderPreviewModel model) {
        this.model = model;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null && shaderInitialized) {
            destroyShader();
        } else if (stage != null && !shaderInitialized && graph != null) {
            createShader(graph);
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
            graphShader = GraphShaderBuilder.buildModelShader("Test", WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

            MapWritablePropertyContainer globalPropertyContainer = new MapWritablePropertyContainer();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Global_Uniform) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    globalPropertyContainer.setValue(property.getName(), propertyType.convertFromJson(property.getData()));
                }
            }
            shaderContext.setGlobalPropertyContainer(globalPropertyContainer);

            localPropertyContainer.clear();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Uniform || property.getLocation() == PropertyLocation.Attribute) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    Object value = propertyType.convertFromJson(property.getData());
                    if (propertyType.isTexture()) {
                        if (value != null) {
                            try {
                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                graphShader.addManagedResource(texture);
                                localPropertyContainer.setValue(property.getName(), new TextureRegion(texture));
                            } catch (Exception exp) {
                                localPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                            }
                        } else {
                            localPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                        }
                    }
                }
            }

            shaderContext.setTimeProvider(timeKeeper);

            rectangleShaderModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);
            sphereShaderModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);

            shaderInitialized = true;
        } catch (Exception exp) {
            exp.printStackTrace();
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
        if (shaderInitialized)
            destroyShader();
        rectangleShaderModel.dispose();
        sphereShaderModel.dispose();
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
                if (model == ShaderPreviewModel.Sphere)
                    graphShader.render(shaderContext, sphereShaderModel);
                else if (model == ShaderPreviewModel.Rectangle)
                    graphShader.render(shaderContext, rectangleShaderModel);
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
        if (hasErrors) {
            this.graph = null;
        } else {
            this.graph = graph;
        }

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

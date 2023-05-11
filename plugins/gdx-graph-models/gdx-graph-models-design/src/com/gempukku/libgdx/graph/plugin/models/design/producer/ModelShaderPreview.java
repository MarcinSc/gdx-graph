package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.ui.shader.GraphShaderRenderingWidget;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.VisTable;

public class ModelShaderPreview extends VisTable implements Disposable {
    public enum ShaderPreviewModel {
        Sphere, Rectangle;
    }

    private final GraphShaderRenderingWidget graphShaderRenderingWidget;

    private GraphWithProperties graph;

    private GraphShader graphShader;
    private MeshBasedRenderableModel renderableModel;

    private final Camera camera;
    private final DefaultTimeKeeper timeKeeper;

    private final MapWritablePropertyContainer globalPropertyContainer;
    private final MapWritablePropertyContainer localPropertyContainer;

    public ModelShaderPreview() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.9f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        timeKeeper = new DefaultTimeKeeper();

        globalPropertyContainer = new MapWritablePropertyContainer();
        localPropertyContainer = new MapWritablePropertyContainer();

        graphShaderRenderingWidget = new GraphShaderRenderingWidget();
        graphShaderRenderingWidget.addPrivatePluginData(Lighting3DPrivateData.class, createLightingPluginData());
        graphShaderRenderingWidget.setColorTexture(PatternTextures.sharedInstance.texture);
        graphShaderRenderingWidget.setDepthTexture(WhitePixel.sharedInstance.texture);
        graphShaderRenderingWidget.setCamera(camera);
        graphShaderRenderingWidget.setTimeProvider(timeKeeper);
        graphShaderRenderingWidget.setGlobalPropertyContainer(globalPropertyContainer);
        graphShaderRenderingWidget.setLocalPropertyContainer(localPropertyContainer);

        renderableModel = createRenderableModel(ShaderPreviewModel.Sphere);
        graphShaderRenderingWidget.setRenderableModel(renderableModel);

        add(graphShaderRenderingWidget).grow();
    }

    private static Lighting3DPrivateData createLightingPluginData() {
        Lighting3DEnvironment graphShaderLightingEnvironment = new Lighting3DEnvironment();
        graphShaderLightingEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -4f, 1.8f, 1.8f, 8f);
        graphShaderLightingEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderLightingEnvironment);
        return data;
    }

    private MeshBasedRenderableModel createRenderableModel(ShaderPreviewModel shaderPreviewModel) {
        Model model = createModel(shaderPreviewModel);

        MeshBasedRenderableModel result = new MeshBasedRenderableModel(model.meshes.get(0));
        model.dispose();

        return result;
    }

    private Model createModel(ShaderPreviewModel shaderPreviewModel) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();

        switch (shaderPreviewModel) {
            case Rectangle:
                return modelBuilder.createRect(
                        0, -0.5f, -0.5f,
                        0, -0.5f, 0.5f,
                        0, 0.5f, 0.5f,
                        0, 0.5f, -0.5f,
                        1, 0, 0,
                        material,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
            case Sphere:
                float sphereDiameter = 0.8f;
                return modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                        material,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        }
        return null;
    }

    public void setModel(ShaderPreviewModel model) {
        if (renderableModel != null) {
            renderableModel.dispose();
        }
        renderableModel = createRenderableModel(model);
        if (graphShader != null) {
            renderableModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);
        }
        graphShaderRenderingWidget.setRenderableModel(renderableModel);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) {
            destroyShader();
        } else if (graphShader == null && graph != null) {
            createShader(graph);
        }
    }

    private void createShader(final GraphWithProperties graph) {
        try {
            graphShader = GraphShaderBuilder.buildModelShader("Test", AssetResolver.instance, graph, true);

            globalPropertyContainer.clear();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Global_Uniform) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    Object value = propertyType.convertFromJson(property.getData());
                    if (propertyType.isTexture()) {
                        if (value != null) {
                            try {
                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                graphShader.addManagedResource(texture);
                                globalPropertyContainer.setValue(property.getName(), new TextureRegion(texture));
                            } catch (Exception exp) {
                                globalPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                            }
                        } else {
                            globalPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                        }
                    } else {
                        globalPropertyContainer.setValue(property.getName(), propertyType.convertFromJson(property.getData()));
                    }
                }
            }

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

            renderableModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);

            graphShaderRenderingWidget.setGraphShader(graphShader);
        } catch (Exception exp) {
            fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            destroyShader();
        }
    }

    private void destroyShader() {
        if (graphShader != null) {
            graphShader.dispose();
            graphShaderRenderingWidget.setGraphShader(null);
            graphShader = null;
        }
    }

    @Override
    public void dispose() {
        destroyShader();
        renderableModel.dispose();
    }

    @Override
    public void act(float delta) {
        // Update time keeper
        timeKeeper.updateTime(Gdx.graphics.getDeltaTime());

        super.act(delta);
    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        destroyShader();
        if (hasErrors) {
            this.graph = null;
        } else {
            this.graph = graph;
            createShader(graph);
        }
    }
}

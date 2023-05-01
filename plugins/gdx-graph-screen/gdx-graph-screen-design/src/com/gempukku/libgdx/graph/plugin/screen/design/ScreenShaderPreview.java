package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.plugin.screen.FullScreenRenderableModel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.ui.shader.GraphShaderRenderingWidget;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.VisTable;

public class ScreenShaderPreview extends VisTable implements Disposable {
    private final GraphShaderRenderingWidget graphShaderRenderingWidget;

    private GraphWithProperties graph;

    private GraphShader graphShader;
    private final FullScreenRenderableModel renderableModel;
    private final FullScreenRenderImpl fullScreenRender;

    private final Camera camera;
    private final DefaultTimeKeeper timeKeeper;

    private final MapWritablePropertyContainer globalPropertyContainer;
    private final MapWritablePropertyContainer localPropertyContainer;

    public ScreenShaderPreview() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.77f, 0f, 0f);
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

        fullScreenRender = new FullScreenRenderImpl();
        renderableModel = new FullScreenRenderableModel();
        renderableModel.setFullScreenRender(fullScreenRender);
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
            graphShader = GraphShaderBuilder.buildScreenShader("Test", WhitePixel.sharedInstance.texture, graph, true);

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
        fullScreenRender.dispose();
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

package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderRenderingWidget;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.shader.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.shader.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.ui.DisposableTable;

public class ShaderPreview extends DisposableTable {
    private final GraphShaderRecipe shaderRecipe;

    private final GraphShaderRenderingWidget graphShaderRenderingWidget;

    private GraphWithProperties graph;

    private GraphShader graphShader;
    private Producer<? extends PreviewRenderableModel> renderableModelProducer;
    private PreviewRenderableModel previewRenderableModel;

    private final Camera camera;
    private final DefaultTimeKeeper timeKeeper;

    private final MapWritablePropertyContainer globalPropertyContainer;
    private final MapWritablePropertyContainer localPropertyContainer;

    private boolean initialized = false;

    public ShaderPreview(GraphShaderRecipe shaderRecipe) {
        this.shaderRecipe = shaderRecipe;
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(0f, 0f, 0.9f);
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

        add(graphShaderRenderingWidget).grow();
    }

    private static Lighting3DPrivateData createLightingPluginData() {
        Lighting3DEnvironment graphShaderLightingEnvironment = new Lighting3DEnvironment();
        graphShaderLightingEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, 1.8f, 1.8f, 4f, 8f);
        graphShaderLightingEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderLightingEnvironment);
        return data;
    }

    private void updateRenderingWidgetIfNeeded() {
        if (initialized && shaderRecipe != null && graph != null && renderableModelProducer != null) {
            // Attempt to compile a shader
            GraphShader graphShader = createShader(graph);
            if (graphShader != null) {
                this.graphShader = graphShader;

                timeKeeper.setTime(0);
                previewRenderableModel = renderableModelProducer.create();
                previewRenderableModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);

                graphShaderRenderingWidget.setGraphShader(graphShader);
                graphShaderRenderingWidget.setRenderableModel(previewRenderableModel);
            }
        } else {
            // Destroy everything!
            destroyShader();
            destroyRenderableModel();
            graphShaderRenderingWidget.setRenderableModel(null);
            graphShaderRenderingWidget.setGraphShader(null);
        }
    }

    public void setRenderableModelProducer(Producer<? extends PreviewRenderableModel> renderableModelProducer) {
        this.renderableModelProducer = renderableModelProducer;
        updateRenderingWidgetIfNeeded();
    }

    @Override
    protected void initializeWidget() {
        initialized = true;
        updateRenderingWidgetIfNeeded();
    }

    @Override
    protected void disposeWidget() {
        initialized = false;
        updateRenderingWidgetIfNeeded();
    }

    private GraphShader createShader(final GraphWithProperties graph) {
        try {
            GraphShader graphShader = shaderRecipe.buildGraphShader("Test", true, graph, AssetResolver.instance);
            try {
                globalPropertyContainer.clear();
                for (GraphProperty property : graph.getProperties()) {
                    PropertyLocation location = PropertyLocation.valueOf(property.getData().getString("location"));
                    if (location == PropertyLocation.Global_Uniform) {
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
                    PropertyLocation location = PropertyLocation.valueOf(property.getData().getString("location"));
                    if (location == PropertyLocation.Uniform || location == PropertyLocation.Attribute) {
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
            } catch (RuntimeException exp) {
                graphShader.dispose();
                throw exp;
            }

            return graphShader;
        } catch (Exception exp) {
            exp.printStackTrace();
            fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            return null;
        }
    }

    private void destroyRenderableModel() {
        if (previewRenderableModel != null) {
            if (previewRenderableModel instanceof Disposable) {
                ((Disposable) previewRenderableModel).dispose();
            }
            previewRenderableModel = null;
        }
    }

    private void destroyShader() {
        if (graphShader != null) {
            graphShader.dispose();
            graphShader = null;
        }
    }

    @Override
    public void act(float delta) {
        timeKeeper.updateTime(Gdx.graphics.getDeltaTime());

        if (previewRenderableModel != null) {
            previewRenderableModel.update(timeKeeper.getTime());
        }

        super.act(delta);
    }

    public void graphChanged(GraphWithProperties graph) {
        boolean valid = shaderRecipe.isValid(graph);
        if (valid) {
            this.graph = graph;
        } else {
            this.graph = null;
        }
        updateRenderingWidgetIfNeeded();
    }
}

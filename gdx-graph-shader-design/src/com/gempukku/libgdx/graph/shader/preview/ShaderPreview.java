package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderRenderingWidget;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.ui.DisposableTable;

public class ShaderPreview extends DisposableTable {
    private final GraphShaderRecipe shaderRecipe;

    private final GraphShaderRenderingWidget graphShaderRenderingWidget;
    private PipelineRendererConfiguration configuration;

    private GraphWithProperties graph;

    private GraphShader graphShader;
    private Producer<? extends PreviewRenderableModel> renderableModelProducer;
    private PreviewRenderableModel previewRenderableModel;

    private final Camera camera;
    private final DefaultTimeKeeper timeKeeper;

    private final MapWritablePropertyContainer rootPropertyContainer;

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
        rootPropertyContainer = new MapWritablePropertyContainer();

        graphShaderRenderingWidget = new GraphShaderRenderingWidget();
        graphShaderRenderingWidget.setColorTexture(PatternTextures.sharedInstance.texture);
        graphShaderRenderingWidget.setDepthTexture(WhitePixel.sharedInstance.texture);
        graphShaderRenderingWidget.setCamera(camera);

        add(graphShaderRenderingWidget).grow();
    }

    private void updateRenderingWidgetIfNeeded() {
        if (initialized && shaderRecipe != null && graph != null && renderableModelProducer != null) {
            // Attempt to compile a shader
            GraphShader graphShader = createShader(graph);
            if (graphShader != null) {
                this.graphShader = graphShader;

                AssetResolver assetResolver = AssetResolver.instance;

                configuration = new PipelineRendererConfiguration(timeKeeper, assetResolver, rootPropertyContainer, null);

                for (ObjectMap.Entry<Class<? extends RendererConfiguration>, Producer<? extends RendererConfiguration>> configurationProducer : UIRenderPipelineConfiguration.getPreviewConfigurationBuilders()) {
                    Class<RendererConfiguration> configurationClass = (Class<RendererConfiguration>) configurationProducer.key;
                    RendererConfiguration rendererConfiguration = configurationProducer.value.create();
                    configuration.setConfig(configurationClass, rendererConfiguration);
                }

                timeKeeper.setTime(0);
                previewRenderableModel = renderableModelProducer.create();

                setupRenderableModel(previewRenderableModel, graph);

                graphShaderRenderingWidget.setGraphShader(graphShader);
            }
        } else {
            // Destroy everything!
            if (configuration != null) {
                configuration.dispose();
                configuration = null;
            }
            destroyShader();
            destroyRenderableModel();
            graphShaderRenderingWidget.setGraphShader(null);
            graphShaderRenderingWidget.setPipelineRendererConfiguration(null);
        }
    }

    private void setupRenderableModel(
            PreviewRenderableModel previewRenderableModel, GraphWithProperties graph) {
        SimpleShaderRendererConfiguration shaderRenderingConfiguration = (SimpleShaderRendererConfiguration) configuration.getConfig(ShaderRendererConfiguration.class);
        shaderRenderingConfiguration.registerShader(graphShader);

        WritablePropertyContainer globalUniforms = shaderRenderingConfiguration.getGlobalUniforms(graphShader);
        globalUniforms.clear();
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
                            globalUniforms.setValue(property.getName(), new TextureRegion(texture));
                        } catch (Exception exp) {
                            globalUniforms.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                        }
                    } else {
                        globalUniforms.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                    }
                } else {
                    globalUniforms.setValue(property.getName(), propertyType.convertFromJson(property.getData()));
                }
            }
        }

        WritablePropertyContainer localPropertyContainer = previewRenderableModel.getPropertyContainer();
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

        previewRenderableModel.updateModel(graphShader.getAttributes(), graphShader.getProperties(), localPropertyContainer);
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
            return shaderRecipe.buildGraphShader("Test", true, graph, configuration);
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

package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.*;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallback;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPublicData;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class Episode3Scene implements LibgdxGraphTestScene {
    private Stage stage;
    private Skin skin;

    private PipelineRenderer pipelineRenderer;
    private Model sphereModel;
    private final Array<RenderableProvider> renderableProviders = new Array<>();
    private Environment environment;
    private Camera camera;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelBatch modelBatch;

    @Override
    public String getName() {
        return "YouTube Episode 3";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = constructStage();

        createModels();

        environment = createEnvironment();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        modelBatch = new ModelBatch();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(3f, 3f, 3f);
        camera.lookAt(0, 0, 0);
        camera.update();
        return camera;
    }

    private Environment createEnvironment() {
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        return environment;
    }

    private void createModels() {
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture), ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.5f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        renderableProviders.add(new ModelInstance(sphereModel));
    }

    private Stage constructStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider bloomRadius = new Slider(0, 64, 1, false, skin);
        bloomRadius.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Bloom Radius", bloomRadius.getValue());
                    }
                });

        final Slider minimalBrightness = new Slider(0, 1, 0.01f, false, skin);
        minimalBrightness.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Min Brightness", minimalBrightness.getValue());
                    }
                });

        final Slider bloomStrength = new Slider(0, 10, 0.01f, false, skin);
        bloomStrength.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Bloom Strength", bloomStrength.getValue());
                    }
                });

        final Slider blurRadius = new Slider(0, 64, 1f, false, skin);
        blurRadius.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Blur Radius", blurRadius.getValue());
                    }
                });

        final Slider gammaCorrection = new Slider(0, 5, 0.01f, false, skin);
        gammaCorrection.setValue(1f);
        gammaCorrection.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Gamma Correction", gammaCorrection.getValue());
                    }
                });

        Table tbl = new Table();
        tbl.add(new Label("Bloom Radius", skin));
        tbl.add(bloomRadius).row();
        tbl.add(new Label("Min Brightness", skin));
        tbl.add(minimalBrightness).row();
        tbl.add(new Label("Bloom Strength", skin));
        tbl.add(bloomStrength).row();
        tbl.add().height(10).colspan(2).row();
        tbl.add(new Label("Blur Radius", skin));
        tbl.add(blurRadius).row();
        tbl.add().height(10).colspan(2).row();
        tbl.add(new Label("Gamma Correction", skin));
        tbl.add(gammaCorrection).row();
        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    private void setPropertyIfDefined(String propertyName, float value) {
        if (pipelineRenderer.hasPipelineProperty(propertyName))
            pipelineRenderer.setPipelineProperty(propertyName, value);
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        renderableProviders.clear();
        modelBatch.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        skin.dispose();
        stage.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode3.json"), timeKeeper);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        pipelineRenderer.getPluginData(RenderCallbackPublicData.class).setRenderCallback(
                "Callback", new RenderCallback() {
                    @Override
                    public void renderCallback(RenderPipeline renderPipeline, PipelineDataProvider pipelineDataProvider, PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback) {
                        RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                        int width = currentBuffer.getWidth();
                        int height = currentBuffer.getHeight();
                        float viewportWidth = camera.viewportWidth;
                        float viewportHeight = camera.viewportHeight;
                        if (width != viewportWidth || height != viewportHeight) {
                            camera.viewportWidth = width;
                            camera.viewportHeight = height;
                            camera.update();
                        }

                        currentBuffer.beginColor();

                        modelBatch.begin(camera);
                        modelBatch.render(renderableProviders, environment);
                        modelBatch.end();

                        currentBuffer.endColor();
                    }
                });
        return pipelineRenderer;
    }
}
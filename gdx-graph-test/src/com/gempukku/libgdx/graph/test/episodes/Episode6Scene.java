package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.impl.SimplePipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

public class Episode6Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model model;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private MaterialModelInstanceModelAdapter modelAdapter;
    private SimplePipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "YouTube Episode 6";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(shaderConfiguration);

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        camera.position.set(0.7f, 0.3f, 1f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
        return camera;
    }

    private void createModels(ModelContainer<RenderableModel> modelContainer) {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.classpath("model/fighter/fighter.g3db"));

        ModelInstance modelInstance = new ModelInstance(model);
        final float scale = 0.0008f;
        modelInstance.transform.idt().scale(scale, scale, scale).rotate(-1, 0, 0f, 90);

        modelAdapter = new MaterialModelInstanceModelAdapter(modelInstance, modelContainer);
        modelAdapter.addTag("Default");
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        final TextButton switchButton = new TextButton("Normal/Hologram", skin, "toggle");
        switchButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        boolean checked = switchButton.isChecked();
                        String removeTag = checked ? "Default" : "Hologram";
                        String tag = checked ? "Hologram" : "Default";
                        modelAdapter.removeTag(removeTag);
                        modelAdapter.addTag(tag);
                    }
                });

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table();
        tbl.add(switchButton).row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
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
        model.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        configuration.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        SimpleUIRendererConfiguration uiConfiguration = new SimpleUIRendererConfiguration();
        uiConfiguration.setStage("", stage);

        configuration = new SimplePipelineRendererConfiguration(timeKeeper);
        configuration.setConfig(UIRendererConfiguration.class, uiConfiguration);
        configuration.getPipelinePropertyContainer().setValue("Camera", camera);

        shaderConfiguration = new SimpleShaderRendererConfiguration(configuration.getPipelinePropertyContainer());
        configuration.setConfig(ShaderRendererConfiguration.class, shaderConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode6.json"), configuration);
    }
}
package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.CommonPropertiesModelInstanceModelAdapter;

public class Episode11Scene implements LibgdxGraphTestScene {
    private final Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Lighting3DEnvironment lights;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelInstance sphereInstance;

    @Override
    public String getName() {
        return "YouTube Episode 11";
    }

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();
        disposables.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getPluginData(GraphModels.class));

        Gdx.input.setInputProcessor(stage);
    }

    private Lighting3DEnvironment createLights() {
        float ambientBrightness = 0.3f;
        float directionalBrightness = 0.8f;
        Lighting3DEnvironment lights = new Lighting3DEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-0.3f, -0.4f, -1);
        lights.addDirectionalLight(new Directional3DLight(directionalLight));
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(8, 0, 5);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0, 0f);
        camera.update();

        return camera;
    }

    private void createModels(GraphModels models) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model forceField = modelBuilder.createRect(
                0, 10, 10,
                0, -10, 10,
                0, -10, -10,
                0, 10, -10,
                0, 0, 1,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(forceField);

        Model sphere = modelBuilder.createSphere(4f, 4f, 4f, 50, 50, new Material(), VertexAttributes.Usage.Position);
        disposables.add(sphere);

        ModelInstance forceFieldInstance = new ModelInstance(forceField);
        sphereInstance = new ModelInstance(sphere);

        CommonPropertiesModelInstanceModelAdapter forceFieldAdapter = new CommonPropertiesModelInstanceModelAdapter(forceFieldInstance, models, new MapWritablePropertyContainer());
        forceFieldAdapter.addTag("force-field");

        CommonPropertiesModelInstanceModelAdapter sphereAdapter = new CommonPropertiesModelInstanceModelAdapter(sphereInstance, models, new MapWritablePropertyContainer());
        sphereAdapter.addTag("default");

        sphereInstance.transform.idt().translate(-3f, 0, 0);
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        final Slider slider = new Slider(-3, 3, 0.01f, false, skin);
        slider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereInstance.transform.idt().translate(slider.getValue(), 0, 0);
                    }
                });
        tbl.add("Sphere translation").padRight(5f);
        tbl.add(slider).width(300f);

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
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode11.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(Lighting3DPublicData.class).setEnvironment("", lights);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        return pipelineRenderer;
    }
}
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
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
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

public class Episode14Scene implements LibgdxGraphTestScene {
    private final Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private Lighting3DEnvironment lights;

    private Model starfield;
    private Model blackHole;
    private Model star;
    private Model starCorona;

    private final Vector3 blackHolePosition = new Vector3(0, 0, 0);
    private final Vector3 starPosition = new Vector3(-10, 0, -10);

    private float cameraPositionAngle;
    private float cameraAngle;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private MaterialModelInstanceModelAdapter starAdapter;
    private MaterialModelInstanceModelAdapter starCoronaAdapter;

    @Override
    public String getName() {
        return "YouTube Episode 14";
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

        updateCamera();
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

        return camera;
    }

    private void createModels(GraphModels models) {
        ModelBuilder modelBuilder = new ModelBuilder();

        starfield = modelBuilder.createSphere(100, 100, 100, 50, 50,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(starfield);

        float blackHoleSize = 10f;
        blackHole = modelBuilder.createSphere(blackHoleSize, blackHoleSize, blackHoleSize, 50, 50,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(blackHole);

        float starSize = 5f;
        star = modelBuilder.createSphere(starSize, starSize, starSize, 50, 50,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(star);

        float coronaMultiplier = 1.4f;
        starCorona = modelBuilder.createSphere(starSize * coronaMultiplier, starSize * coronaMultiplier, starSize * coronaMultiplier, 50, 50,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(starCorona);

        registerModels(models);
    }

    private void registerModels(GraphModels models) {
        ModelInstance starfieldInstance = new ModelInstance(starfield);
        MaterialModelInstanceModelAdapter starfieldAdapter = new MaterialModelInstanceModelAdapter(starfieldInstance, models);
        starfieldAdapter.addTag("starfield");

        ModelInstance blackHoleInstance = new ModelInstance(blackHole);
        MaterialModelInstanceModelAdapter blackHoleAdapter = new MaterialModelInstanceModelAdapter(blackHoleInstance, models);
        blackHoleAdapter.addTag("black-hole");

        ModelInstance starInstance = new ModelInstance(star);
        starInstance.transform.idt().translate(starPosition.x, starPosition.y, starPosition.z);
        starAdapter = new MaterialModelInstanceModelAdapter(starInstance, models);

        ModelInstance starCoronaInstance = new ModelInstance(starCorona);
        starCoronaInstance.transform.idt().translate(starPosition.x, starPosition.y, starPosition.z);
        starCoronaAdapter = new MaterialModelInstanceModelAdapter(starCoronaInstance, models);
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        final Slider positionAngle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        positionAngle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraPositionAngle = positionAngle.getValue();
                        updateCamera();
                    }
                });

        final Slider angle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        angle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraAngle = angle.getValue();
                        updateCamera();
                    }
                });

        tbl.add("Camera orbit");
        tbl.add(positionAngle).width(500);
        tbl.row();

        tbl.add("Camera rotation");
        tbl.add(angle).width(500);
        tbl.row();

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        float cameraDistance = 30f;

        camera.position.set(cameraDistance * MathUtils.cos(cameraPositionAngle), 0, cameraDistance * MathUtils.sin(cameraPositionAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0f, 0f, 0f);
        camera.rotate(MathUtils.radDeg * cameraAngle, 0, 1, 0);
        camera.update();

        float distanceToBlackHole = blackHolePosition.dst2(camera.position);
        float distanceToStar = starPosition.dst2(camera.position);
        if (distanceToBlackHole < distanceToStar) {
            if (!starAdapter.hasTag("star-surface-behind")) {
                starAdapter.addTag("star-surface-behind");
                starCoronaAdapter.addTag("star-corona-behind");
            }
            if (starAdapter.hasTag("star-surface-in-front")) {
                starAdapter.removeTag("star-surface-in-front");
                starCoronaAdapter.removeTag("star-corona-in-front");
            }
        } else {
            if (starAdapter.hasTag("star-surface-behind")) {
                starAdapter.removeTag("star-surface-behind");
                starCoronaAdapter.removeTag("star-corona-behind");
            }
            if (!starAdapter.hasTag("star-surface-in-front")) {
                starAdapter.addTag("star-surface-in-front");
                starCoronaAdapter.addTag("star-corona-in-front");
            }
        }
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
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode14.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(Lighting3DPublicData.class).setEnvironment("", lights);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        return pipelineRenderer;
    }
}
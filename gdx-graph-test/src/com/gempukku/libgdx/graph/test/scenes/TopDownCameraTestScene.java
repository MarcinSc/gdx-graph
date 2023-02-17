package com.gempukku.libgdx.graph.test.scenes;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteBatchSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;
import com.gempukku.libgdx.lib.artemis.camera.ScreenResized;
import com.gempukku.libgdx.lib.artemis.camera.topdown.TopDownCameraController;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RuntimeEntityEventDispatcher;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorSystem;
import com.gempukku.libgdx.lib.artemis.spawn.SpawnSystem;
import com.gempukku.libgdx.lib.artemis.texture.RuntimeTextureHandler;
import com.gempukku.libgdx.lib.artemis.texture.TextureSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class TopDownCameraTestScene implements LibgdxGraphTestScene {
    private static final int INDEPENDENT_SYSTEMS = 4;
    private static final int DEPEND_ON_CAMERA_SYSTEMS = 3;
    private static final int DEPEND_ON_RENDERER_SYSTEMS = 2;
    private static final int DEPEND_ON_BATCH_SYSTEMS = 1;
    private static final int LAST_PROCESSING = 0;

    private World world;

    private Skin skin;
    private Stage stage;

    @Override
    public String getName() {
        return "Top Down Camera";
    }

    @Override
    public void initializeScene() {
        createSystems();

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/camera/topdown/top-down-camera-setup.entities");

        world.process();

        spawnSystem.spawnEntities("entity/camera/topdown/top-down-camera-environment.entities");

        createUI();
    }

    private void createSystems() {
        final TextureSystem textureSystem = new TextureSystem();
        textureSystem.setDefaultTextureHandler(new RuntimeTextureHandler());

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder();
        worldConfigurationBuilder.alwaysDelayComponentRemoval(true);
        worldConfigurationBuilder.with(INDEPENDENT_SYSTEMS,
                new TimeKeepingSystem(),
                new SpawnSystem(),
                new EventSystem(new RuntimeEntityEventDispatcher()),
                new EvaluatePropertySystem(),
                new TransformSystem(),
                new HierarchySystem(),
                textureSystem,
                new CameraSystem(new TopDownCameraController()),
                new InputProcessorSystem());
        worldConfigurationBuilder.with(DEPEND_ON_CAMERA_SYSTEMS,
                new PipelineRendererSystem());
        worldConfigurationBuilder.with(DEPEND_ON_RENDERER_SYSTEMS,
                new SpriteBatchSystem());
        worldConfigurationBuilder.with(DEPEND_ON_BATCH_SYSTEMS,
                new SpriteSystem());

        world = new World(worldConfigurationBuilder.build());

        world.getSystem(InputProcessorSystem.class).setupProcessing();
    }

    private void createUI() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        final Matrix4 tmpMatrix = new Matrix4();

        final Label rotationLabel = new Label("Rotation: 0deg", skin);
        final Slider rotationSlider = new Slider(0, 360, 1, false, skin);
        rotationSlider.setValue(0f);
        rotationSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        TopDownCameraController cameraController = (TopDownCameraController) world.getSystem(CameraSystem.class).getCameraController("Main");
                        float rotation = rotationSlider.getValue();
                        cameraController.setRotation("Main", rotation);
                        rotationLabel.setText("Rotation: " + rotation + "deg");
                    }
                });
        final Label angleLabel = new Label("Angle: 35deg", skin);
        final Slider angleSlider = new Slider(-89, 89, 1f, false, skin);
        angleSlider.setValue(35f);
        angleSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        TopDownCameraController cameraController = (TopDownCameraController) world.getSystem(CameraSystem.class).getCameraController("Main");
                        float angle = angleSlider.getValue();
                        cameraController.setAngle("Main", angle);
                        angleLabel.setText("Angle: " + angle + "deg");
                    }
                });
        final Label tiltLabel = new Label("Tilt: 0deg", skin);
        final Slider tiltSlider = new Slider(-89, 89, 1f, false, skin);
        tiltSlider.setValue(0f);
        tiltSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        TopDownCameraController cameraController = (TopDownCameraController) world.getSystem(CameraSystem.class).getCameraController("Main");
                        float angle = tiltSlider.getValue();
                        cameraController.setTilt("Main", angle);
                        tiltLabel.setText("Tilt: " + angle + "deg");
                    }
                });

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        tbl.add(rotationLabel).pad(10f, 10f, 0f, 10f).row();
        tbl.add(rotationSlider).pad(0, 10f, 0, 10f).row();
        tbl.add(angleLabel).pad(10f, 10f, 0f, 10f).row();
        tbl.add(angleSlider).pad(0, 10f, 0, 10f).row();
        tbl.add(tiltLabel).pad(10f, 10f, 0, 10f).row();
        tbl.add(tiltSlider).pad(0f, 10f, 0, 10f).row();

        stage.addActor(tbl);

        world.getSystem(PipelineRendererSystem.class).getPluginData(UIPluginPublicData.class).setStage("Main", stage);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);

        EventSystem eventSystem = world.getSystem(EventSystem.class);
        eventSystem.fireEvent(new ScreenResized(width, height), null);
    }

    @Override
    public void renderScene() {
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        world.setDelta(delta);

        stage.act();

        world.process();
    }

    @Override
    public void disposeScene() {
        world.dispose();
        skin.dispose();
        stage.dispose();
    }
}

package com.gempukku.libgdx.graph.test.scenes;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
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

public class HierarchyAndTransformTestScene implements LibgdxGraphTestScene {
    private static final int INDEPENDENT_SYSTEMS = 4;
    private static final int DEPEND_ON_CAMERA_SYSTEMS = 3;
    private static final int DEPEND_ON_RENDERER_SYSTEMS = 2;
    private static final int DEPEND_ON_BATCH_SYSTEMS = 1;
    private static final int LAST_PROCESSING = 0;

    private World world;
    private Entity parentEntity;
    private Entity childEntity;

    private Skin skin;
    private Stage stage;

    @Override
    public void initializeScene() {
        createSystems();

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/transform/transform-setup.entities");

        world.process();

        parentEntity = spawnSystem.spawnEntity("entity/transform/parent.template");
        childEntity = spawnSystem.spawnEntity("entity/transform/child.template");

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
        stage = new Stage();

        final Matrix4 tmpMatrix = new Matrix4();

        Label parentRotationLabel = new Label("Parent rotation", skin);
        final Slider parentRotationSlider = new Slider(-85, 85, 1, false, skin);
        parentRotationSlider.setValue(0f);
        parentRotationSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        tmpMatrix.idt().setToRotation(0, 1, 0, parentRotationSlider.getValue());
                        world.getSystem(TransformSystem.class).setTransform(parentEntity, tmpMatrix);
                    }
                });
        Label childTranslationLabel = new Label("Child translation", skin);
        final Slider childTranslationSlider = new Slider(-2, 2, 0.01f, false, skin);
        childTranslationSlider.setValue(0f);
        childTranslationSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        tmpMatrix.idt().setToTranslation(childTranslationSlider.getValue(), 0, 0);
                        world.getSystem(TransformSystem.class).setTransform(childEntity, tmpMatrix);
                    }
                });
        final CheckBox connectedCheckBox = new CheckBox("Connect entities", skin);
        connectedCheckBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (connectedCheckBox.isChecked()) {
                            world.getSystem(HierarchySystem.class).addHierarchy(parentEntity, childEntity);
                        } else {
                            world.getSystem(HierarchySystem.class).removeHierarchy(childEntity);
                        }
                    }
                });

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topRight);

        tbl.add(parentRotationLabel).pad(10f, 10f, 0f, 10f).row();
        tbl.add(parentRotationSlider).pad(0, 10f, 0, 10f).row();
        tbl.add(childTranslationLabel).pad(10f, 10f, 0, 10f).row();
        tbl.add(childTranslationSlider).pad(0f, 10f, 0, 10f).row();
        tbl.add(connectedCheckBox).pad(10f).row();

        stage.addActor(tbl);

        world.getSystem(PipelineRendererSystem.class).getPluginData(UIPluginPublicData.class).setStage("Main", stage);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resizeScene(int width, int height) {
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

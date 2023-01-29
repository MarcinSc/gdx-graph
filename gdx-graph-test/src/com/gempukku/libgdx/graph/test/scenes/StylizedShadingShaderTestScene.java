package com.gempukku.libgdx.graph.test.scenes;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.artemis.lighting.LightingSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.PatchGeneratorSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.PatchworkSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.ArrowGenerator;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.ConeGenerator;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.SphereGenerator;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
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

public class StylizedShadingShaderTestScene implements LibgdxGraphTestScene {
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
        return "Stylized Shading";
    }

    @Override
    public void initializeScene() {
        createSystems();

        PatchGeneratorSystem patchGeneratorSystem = world.getSystem(PatchGeneratorSystem.class);
        patchGeneratorSystem.registerPatchGenerator("sphere", new SphereGenerator());
        patchGeneratorSystem.registerPatchGenerator("cone", new ConeGenerator());
        patchGeneratorSystem.registerPatchGenerator("arrow", new ArrowGenerator());

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/shading/shading-setup.entities");

        world.process();

        spawnSystem.spawnEntity("entity/shading/sphere.template");
        spawnSystem.spawnEntity("entity/shading/cone.template");
        spawnSystem.spawnEntity("entity/shading/arrow.template");

        createUI();
    }

    private void appendPosition(StringBuilder result, FloatArray positionArray, short index) {
        result.append(positionArray.get(index * 3) + ", " + positionArray.get(index * 3 + 1) + ", " + positionArray.get(index * 3 + 2));
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
                new PatchworkSystem(),
                new LightingSystem());
        worldConfigurationBuilder.with(DEPEND_ON_BATCH_SYSTEMS,
                new PatchGeneratorSystem());

        world = new World(worldConfigurationBuilder.build());

        world.getSystem(InputProcessorSystem.class).setupProcessing();
    }

    private void createUI() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

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

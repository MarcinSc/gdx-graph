package com.gempukku.libgdx.graph.test.scenes;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.artemis.lighting.LightingSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.PatchGeneratorSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.PatchworkSystem;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.ArrowGenerator;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.BoxGenerator;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.ConeGenerator;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.SphereGenerator;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.artemis.ui.StageSystem;
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

    private enum ShadingTexture {
        Circles("Circles", "image/circle-tiling.png"),
        CrossHatch("Crosshatch", "image/cross-hatch.png");

        private String text;
        private String texture;

        ShadingTexture(String text, String texture) {
            this.text = text;
            this.texture = texture;
        }

        public String getTexture() {
            return texture;
        }

        public String toString() {
            return text;
        }
    }

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
        patchGeneratorSystem.registerPatchGenerator("box", new BoxGenerator());

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/shading/shading-setup.entities");

        PipelineRendererSystem pipelineRenderSystem = world.getSystem(PipelineRendererSystem.class);
        pipelineRenderSystem.setRenderingEnabled(false);

        world.process();

        String texture = ShadingTexture.Circles.getTexture();
        setShadingTexture(pipelineRenderSystem, texture);

        spawnSystem.spawnEntities("entity/shading/shading-environment.entities");

        createUI();

        pipelineRenderSystem.setRenderingEnabled(true);
    }

    private void setShadingTexture(PipelineRendererSystem pipelineRenderSystem, String texture) {
        pipelineRenderSystem.setGlobalUniform("Stylized",
                "Shading Texture",
                world.getSystem(TextureSystem.class).getTextureRegion(texture, texture));
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
                new LightingSystem(1, 0, 0, 0.05f, 1));
        worldConfigurationBuilder.with(DEPEND_ON_BATCH_SYSTEMS,
                new PatchGeneratorSystem(), new StageSystem(0));

        world = new World(worldConfigurationBuilder.build());

        world.getSystem(InputProcessorSystem.class).setupProcessing();
    }

    private void createUI() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        Label cameraPositionLabel = new Label("Camera position", skin);
        final Slider cameraPositionAngle = new Slider(0, 360f, 0.1f, false, skin);
        cameraPositionAngle.setValue(0);
        cameraPositionAngle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        TopDownCameraController cameraController = (TopDownCameraController) world.getSystem(CameraSystem.class).getCameraController("Main");
                        cameraController.setRotation("Main", cameraPositionAngle.getValue());
                    }
                });
        tbl.add(cameraPositionLabel).width(300).pad(10, 10, 0, 10).row();
        tbl.add(cameraPositionAngle).width(300).pad(0, 10, 0, 10).row();

        Label shadingTextureLabel = new Label("Shading texture", skin);
        final SelectBox<ShadingTexture> shadingTexture = new SelectBox<>(skin);
        shadingTexture.setItems(ShadingTexture.values());
        shadingTexture.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setShadingTexture(world.getSystem(PipelineRendererSystem.class), shadingTexture.getSelected().getTexture());
                    }
                });
        tbl.add(shadingTextureLabel).width(300).pad(10, 10, 0, 10).row();
        tbl.add(shadingTexture).width(300).pad(0, 10, 0, 10).row();

        Label textureScaleLabel = new Label("Texture scale", skin);
        final Slider textureScale = new Slider(0.2f, 3.0f, 0.1f, false, skin);
        textureScale.setValue(1f);
        textureScale.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        world.getSystem(PipelineRendererSystem.class).setGlobalUniform("Stylized",
                                "Texture Scale", textureScale.getValue());
                    }
                });
        tbl.add(textureScaleLabel).width(300).pad(10, 10, 0, 10).row();
        tbl.add(textureScale).width(300).pad(0, 10, 0, 10).row();

        stage.addActor(tbl);

        world.getSystem(StageSystem.class).addStage("Main", stage, true);
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
    }
}

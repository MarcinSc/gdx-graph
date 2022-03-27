package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.entity.EntityLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.system.*;
import com.gempukku.libgdx.graph.system.camera.FocusCameraController;
import com.gempukku.libgdx.graph.system.camera.constraint.SceneCameraConstraint;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.CameraFocusConstraint;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.FixedToWindowCameraConstraint;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.SnapToWindowCameraConstraint;
import com.gempukku.libgdx.graph.system.camera.focus.SpriteAdvanceFocus;
import com.gempukku.libgdx.graph.system.sensor.FootSensorContactListener;
import com.gempukku.libgdx.graph.system.sensor.InteractSensorContactListener;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.particles.CommonPropertiesParticleEffectAdapter;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.ParallelogramPositionGenerator;

public class Episode24Scene implements LibgdxGraphTestScene {
    private final Array<Disposable> resources = new Array<>();
    private PipelineRenderer pipelineRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();

    private Engine engine;
    private TextureHolder textureHolder;

    private final boolean debugRender = false;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 tmpMatrix;

    @Override
    public void initializeScene() {
        Box2D.init();
        WhitePixel.initialize();

        skin = createSkin();
        resources.add(skin);

        stage = createStage(skin);
        resources.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        resources.add(pipelineRenderer);

        textureHolder = new TextureHolder();
        resources.add(textureHolder);

        createSystems();

        Json json = new Json();

        loadEnvironment(json);

        Entity playerEntity = EntityLoader.readEntity(engine, json, "sprite/playerBlueWizard.json");
        engine.getSystem(PlayerControlSystem.class).setPlayerEntity(playerEntity);

        FocusCameraController cameraController = new FocusCameraController(camera,
                // Try to focus on the point 200 pixels in front of player entity,
                new SpriteAdvanceFocus(playerEntity, 200f),
                new CameraFocusConstraint[]{
                        // Move the camera to try to keep the focus point within the middle 10% of the screen, camera movement speed is 20% of screen/second
                        new SnapToWindowCameraConstraint(new Rectangle(0.45f, 0.45f, 0.1f, 0.1f), new Vector2(0.2f, 0.2f)),
                        // Move the camera to make sure the focused point is in the middle 50% of the screen
                        new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f))
                },
                // Move the camera to make sure that pixels outside of the scene bounds are not shown
                new SceneCameraConstraint(new Rectangle(-2560, -414, 5120, 2000)));
        engine.getSystem(CameraSystem.class).setConstraintCameraController(cameraController);

        Gdx.input.setInputProcessor(stage);

        if (debugRender) {
            debugRenderer = new Box2DDebugRenderer();
            tmpMatrix = new Matrix4();
        }
    }

    private void loadEnvironment(Json json) {
        EntityLoader.readEntity(engine, json, "sprite/ground.json");
        EntityLoader.readEntity(engine, json, "sprite/hill1.json");
        EntityLoader.readEntity(engine, json, "sprite/hill2.json");
        EntityLoader.readEntity(engine, json, "sprite/hill3.json");
        EntityLoader.readEntity(engine, json, "sprite/hill4.json");
        EntityLoader.readEntity(engine, json, "sprite/jumpPlant.json");
        EntityLoader.readEntity(engine, json, "sprite/slime.json");
        EntityLoader.readEntity(engine, json, "sprite/characterPortrait.json");

        GraphParticleEffects particleEffects = pipelineRenderer.getPluginData(GraphParticleEffects.class);
        final ParallelogramPositionGenerator positionGenerator = new ParallelogramPositionGenerator();
        positionGenerator.getOrigin().set(0, 0, -10);
        positionGenerator.getDirection1().set(1, 0, 0);
        positionGenerator.getDirection2().set(0, 1, 0);
        DefaultParticleGenerator particleGenerator = new DefaultParticleGenerator(timeKeeper, 5, 0, 20) {
            @Override
            protected void generateAttributes(ObjectMap attributes) {
                attributes.put("X movement", MathUtils.random(-50f, 50f));
                attributes.put("Y movement", MathUtils.random(-50f, 50f));
                attributes.put("Parallax layer", MathUtils.random(0.8f, 0.99f));
                attributes.put("Size", MathUtils.random(5f, 15f));
                attributes.put("Color", MathUtils.random(0f, 1f));
            }
        };
        particleGenerator.setPositionGenerator(positionGenerator);
        CommonPropertiesParticleEffectAdapter particleEffectAdapter = new CommonPropertiesParticleEffectAdapter(particleEffects);
        particleEffectAdapter.addTag("Dust", particleGenerator);
        particleEffectAdapter.startEffect("Dust");
    }

    private void createSystems() {
        engine = new Engine();

        engine.addSystem(new PlayerControlSystem(20));

        PhysicsSystem physicsSystem = new PhysicsSystem(30, -30f);
        physicsSystem.addSensorContactListener("foot", new FootSensorContactListener());
        physicsSystem.addSensorContactListener("interact", new InteractSensorContactListener());
        engine.addSystem(physicsSystem);

        engine.addSystem(new OutlineSystem(35, pipelineRenderer));

        engine.addSystem(new RenderingSystem(40, timeKeeper, pipelineRenderer, textureHolder));

        engine.addSystem(new CameraSystem(50));
    }


    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }

    private Skin createSkin() {
        return new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
    }

    private Stage createStage(Skin skin) {
        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

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
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        timeKeeper.updateTime(delta);
        stage.act(delta);

        engine.update(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);

        if (debugRender) {
            tmpMatrix.set(camera.combined).scale(PhysicsSystem.PIXELS_TO_METERS, PhysicsSystem.PIXELS_TO_METERS, 0);
            debugRenderer.render(engine.getSystem(PhysicsSystem.class).getWorld(), tmpMatrix);
        }
    }

    @Override
    public void disposeScene() {
        for (EntitySystem system : engine.getSystems()) {
            if (system instanceof Disposable)
                ((Disposable) system).dispose();
        }

        for (Disposable resource : resources) {
            resource.dispose();
        }
        resources.clear();

        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode24.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        return pipelineRenderer;
    }
}
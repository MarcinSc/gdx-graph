package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.entity.EntityLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.system.*;
import com.gempukku.libgdx.graph.system.camera.FocusCameraController;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.CameraFocusConstraint;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.FixedToWindowCameraConstraint;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.SnapToWindowCameraConstraint;
import com.gempukku.libgdx.graph.system.camera.focus.SpriteFocus;
import com.gempukku.libgdx.graph.system.sensor.FootSensorContactListener;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class Episode19Scene implements LibgdxGraphTestScene {
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
        FocusCameraController cameraController = new FocusCameraController(camera, new SpriteFocus(playerEntity),
                new CameraFocusConstraint[]{
                        new SnapToWindowCameraConstraint(new Rectangle(0.2f, 0.1f, 0.2f, 0.4f), new Vector2(0.1f, 0.1f)),
                        new FixedToWindowCameraConstraint(new Rectangle(0.1f, 0.1f, 0.4f, 0.6f))
                });
        engine.getSystem(CameraSystem.class).setConstraintCameraController(cameraController);

        Gdx.input.setInputProcessor(stage);

        if (debugRender) {
            debugRenderer = new Box2DDebugRenderer();
            tmpMatrix = new Matrix4();
        }
    }

    private void loadEnvironment(Json json) {
        EntityLoader.readEntity(engine, json, "sprite/ground.json");
    }

    private void createSystems() {
        engine = new Engine();

        engine.addSystem(new PlayerControlSystem(20));

        PhysicsSystem physicsSystem = new PhysicsSystem(30, -30f);
        physicsSystem.addSensorContactListener("foot", new FootSensorContactListener());
        engine.addSystem(physicsSystem);

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
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode19.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
        return pipelineRenderer;
    }
}
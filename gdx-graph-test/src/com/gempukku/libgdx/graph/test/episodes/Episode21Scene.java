package com.gempukku.libgdx.graph.test.episodes;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.gempukku.libgdx.box2d.artemis.PhysicsSystem;
import com.gempukku.libgdx.camera2d.Camera2DController;
import com.gempukku.libgdx.camera2d.constraint.LockedToWindowCamera2DConstraint;
import com.gempukku.libgdx.camera2d.constraint.SceneCamera2DConstraint;
import com.gempukku.libgdx.camera2d.constraint.SnapToWindowCamera2DConstraint;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteBatchSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.system.ConfigurePhysicsSystem;
import com.gempukku.libgdx.graph.test.system.OutlineSystem;
import com.gempukku.libgdx.graph.test.system.PlayerControlSystem;
import com.gempukku.libgdx.graph.test.system.camera.PlayerAdvanceFocus;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;
import com.gempukku.libgdx.lib.artemis.camera.ScreenResized;
import com.gempukku.libgdx.lib.artemis.camera.orthographic.OrthographicCameraControlSystem;
import com.gempukku.libgdx.lib.artemis.camera.orthographic.OrthographicCameraController;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RuntimeEntityEventDispatcher;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorSystem;
import com.gempukku.libgdx.lib.artemis.input.UserInputSystem;
import com.gempukku.libgdx.lib.artemis.spawn.SpawnSystem;
import com.gempukku.libgdx.lib.artemis.texture.RuntimeTextureHandler;
import com.gempukku.libgdx.lib.artemis.texture.TextureSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class Episode21Scene implements LibgdxGraphTestScene {
    private final static float pixelsToMeters = 100f;

    private static final int INDEPENDENT_SYSTEMS = 4;
    private static final int DEPEND_ON_CAMERA_SYSTEMS = 3;
    private static final int DEPEND_ON_RENDERER_SYSTEMS = 2;
    private static final int DEPEND_ON_BATCH_SYSTEMS = 1;
    private static final int LAST_PROCESSING = 0;

    private World world;

    private final boolean debugRender = false;
    private Box2DDebugRenderer debugRenderer;

    private final Matrix4 tmpMatrix4 = new Matrix4();
    private final Vector3 tmpVector3 = new Vector3();

    @Override
    public String getName() {
        return "YouTube Episode 21";
    }

    @Override
    public void initializeScene() {
        Box2D.init();

        createSystems();

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/2dscene/episode21/episode21-setup.entities");

        world.process();

        // Load environment
        spawnSystem.spawnEntities("entity/2dscene/episode21/episode21-environment.entities");

        // Load player
        final Entity playerEntity = spawnSystem.spawnEntity("entity/2dscene/playerBlueWizard2.template");

        // Setup camera to track player
        Camera2DController cameraController = new Camera2DController(// Try to focus on the point 200 pixels in front of player entity,
                new PlayerAdvanceFocus(world.getSystem(TransformSystem.class), playerEntity, 200f),
                // Move the camera to try to keep the focus point within the middle 10% of the screen, camera movement speed is 20% of screen/second
                new SnapToWindowCamera2DConstraint(new Rectangle(0.45f, 0.45f, 0.1f, 0.1f), new Vector2(0.2f, 0.2f)),
                // Move the camera to make sure the focused point is in the middle 50% of the screen
                new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f)),
                // Move the camera to make sure that pixels outside of the scene bounds are not shown
                new SceneCamera2DConstraint(new Rectangle(-2560, -414, 5120, 2000))
        );
        world.getSystem(OrthographicCameraControlSystem.class).setCameraController("Main", cameraController);

        if (debugRender) {
            debugRenderer = new Box2DDebugRenderer();
        }
    }

    private void createSystems() {
        TextureSystem textureSystem = new TextureSystem();
        textureSystem.setDefaultTextureHandler(new RuntimeTextureHandler());

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder();
        worldConfigurationBuilder.alwaysDelayComponentRemoval(true);
        worldConfigurationBuilder.with(INDEPENDENT_SYSTEMS,
                new TimeKeepingSystem(),
                new SpawnSystem(),
                new EventSystem(new RuntimeEntityEventDispatcher()),
                new EvaluatePropertySystem(),
                new TransformSystem(),
                textureSystem,
                new CameraSystem(new OrthographicCameraController()),
                new InputProcessorSystem(),
                new UserInputSystem(1),
                new PhysicsSystem(new Vector2(0, -30f), pixelsToMeters),
                new ConfigurePhysicsSystem());
        worldConfigurationBuilder.with(DEPEND_ON_CAMERA_SYSTEMS,
                new PipelineRendererSystem(),
                new OrthographicCameraControlSystem(),
                new PlayerControlSystem());
        worldConfigurationBuilder.with(DEPEND_ON_RENDERER_SYSTEMS,
                new SpriteBatchSystem());
        worldConfigurationBuilder.with(DEPEND_ON_BATCH_SYSTEMS,
                new SpriteSystem(),
                new OutlineSystem());

        world = new World(worldConfigurationBuilder.build());

        world.getSystem(InputProcessorSystem.class).setupProcessing();
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
        world.process();

        if (debugRender) {
            tmpMatrix4.set(world.getSystem(CameraSystem.class).getCamera("Main").combined).scale(pixelsToMeters, pixelsToMeters, 0);
            debugRenderer.render(world.getSystem(PhysicsSystem.class).getBox2DWorld(), tmpMatrix4);
        }
    }

    @Override
    public void disposeScene() {
        world.dispose();
    }
}
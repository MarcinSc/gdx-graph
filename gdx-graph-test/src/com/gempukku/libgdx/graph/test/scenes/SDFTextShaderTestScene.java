package com.gempukku.libgdx.graph.test.scenes;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteBatchSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.text.TextSystem;
import com.gempukku.libgdx.graph.artemis.text.parser.html.HtmlTextParser;
import com.gempukku.libgdx.graph.artemis.time.TimeKeepingSystem;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;
import com.gempukku.libgdx.lib.artemis.camera.ScreenResized;
import com.gempukku.libgdx.lib.artemis.camera.topdown.TopDownCameraController;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RuntimeEntityEventDispatcher;
import com.gempukku.libgdx.lib.artemis.font.BitmapFontSystem;
import com.gempukku.libgdx.lib.artemis.font.RuntimeBitmapFontHandler;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorSystem;
import com.gempukku.libgdx.lib.artemis.spawn.SpawnSystem;
import com.gempukku.libgdx.lib.artemis.texture.RuntimeTextureHandler;
import com.gempukku.libgdx.lib.artemis.texture.TextureSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

import java.util.function.Function;

public class SDFTextShaderTestScene implements LibgdxGraphTestScene {
    private static final int INDEPENDENT_SYSTEMS = 4;
    private static final int DEPEND_ON_CAMERA_SYSTEMS = 3;
    private static final int DEPEND_ON_RENDERER_SYSTEMS = 2;
    private static final int DEPEND_ON_BATCH_SYSTEMS = 1;
    private static final int LAST_PROCESSING = 0;

    private World world;

    @Override
    public String getName() {
        return "SDF Text Test";
    }

    @Override
    public void initializeScene() {
        createSystems();

        SpawnSystem spawnSystem = world.getSystem(SpawnSystem.class);
        spawnSystem.spawnEntities("entity/sdf-text/sdf-text-setup.entities");

        world.process();

        spawnSystem.spawnEntity("entity/sdf-text/sdf-text-example.template");
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
                textureSystem,
                new CameraSystem(new TopDownCameraController()),
                new InputProcessorSystem(),
                new BitmapFontSystem(new RuntimeBitmapFontHandler()));
        worldConfigurationBuilder.with(DEPEND_ON_CAMERA_SYSTEMS,
                new PipelineRendererSystem());
        worldConfigurationBuilder.with(DEPEND_ON_RENDERER_SYSTEMS,
                new SpriteBatchSystem());
        worldConfigurationBuilder.with(DEPEND_ON_BATCH_SYSTEMS,
                new SpriteSystem(),
                new TextSystem());

        world = new World(worldConfigurationBuilder.build());

        world.getSystem(InputProcessorSystem.class).setupProcessing();

        HtmlTextParser htmlTextParser = new HtmlTextParser(
                new Function<String, BitmapFont>() {
                    @Override
                    public BitmapFont apply(String s) {
                        return world.getSystem(BitmapFontSystem.class).getBitmapFont(s);
                    }
                },
                new Function<String, TextureRegion>() {
                    @Override
                    public TextureRegion apply(String s) {
                        String[] params = s.split(":", 2);
                        return textureSystem.getTextureRegion(params[0], params[1]);
                    }
                });
        world.getSystem(TextSystem.class).setDefaultTextParser(htmlTextParser);
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
    }

    @Override
    public void disposeScene() {
        world.dispose();
    }
}
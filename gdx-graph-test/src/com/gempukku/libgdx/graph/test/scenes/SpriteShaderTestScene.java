package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.sprite.DefaultRenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;

public class SpriteShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;
    private SpriteBatchModel spriteBatch;

    @Override
    public void initializeScene() {
        camera = new OrthographicCamera();
        pipelineRenderer = loadPipelineRenderer();

        ArrayValuePerVertex<Vector2> uvPerVertex = new ArrayValuePerVertex<>(
                new Vector2(0, 0), new Vector2(1, 0), new Vector2(0, 1), new Vector2(1, 1));

        GraphModels graphModels = pipelineRenderer.getPluginData(GraphModels.class);

        spriteBatch = new SpriteBatchModel(true, 2, graphModels, "Test");

        MapWritablePropertyContainer sprite1 = new MapWritablePropertyContainer();
        sprite1.setValue("Position", new Vector3(0, 0, -10));
        sprite1.setValue("UV", uvPerVertex);
        ArrayValuePerVertex<Vector2> colorPerVertex = new ArrayValuePerVertex<>(
                new Vector2(0, 1), new Vector2(1, 0), new Vector2(0, 0), new Vector2(1, 1));
        sprite1.setValue("Vertex Color", colorPerVertex);

        MapWritablePropertyContainer sprite2 = new MapWritablePropertyContainer();
        sprite2.setValue("Position", new Vector3(150, 0, -10));
        sprite2.setValue("UV", uvPerVertex);

        spriteBatch.addSprite(new DefaultRenderableSprite(sprite1));
        spriteBatch.addSprite(new DefaultRenderableSprite(sprite2));

        graphModels.setGlobalProperty("Test", "Color", new Vector2(1f, 1f));
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        spriteBatch.dispose();
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("test/sprite-shader-test.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        return pipelineRenderer;
    }
}
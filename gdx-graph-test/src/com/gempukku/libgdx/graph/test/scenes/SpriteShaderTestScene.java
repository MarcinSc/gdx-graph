package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.sprites.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.sprite.CommonPropertiesSpriteAdapter;

public class SpriteShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;

    @Override
    public void initializeScene() {
        camera = new OrthographicCamera();
        pipelineRenderer = loadPipelineRenderer();

        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);

        CommonPropertiesSpriteAdapter renderableSprite1 = new CommonPropertiesSpriteAdapter(graphSprites, new Vector3(0, 0, -10));
        ArrayValuePerVertex colorPerVertex = new ArrayValuePerVertex(
                new Vector2(0, 1), new Vector2(1, 0), new Vector2(0, 0), new Vector2(1, 1));
        renderableSprite1.getPropertyContainer().setValue("Vertex Color", colorPerVertex);

        CommonPropertiesSpriteAdapter renderableSprite2 = new CommonPropertiesSpriteAdapter(graphSprites, new Vector3(150, 0, -10));

        renderableSprite1.addTag("Test");
        renderableSprite2.addTag("Test");

        graphSprites.setGlobalProperty("Test", "Color", new Vector2(1f, 1f));
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
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("test/sprite-shader-test.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        return pipelineRenderer;
    }
}
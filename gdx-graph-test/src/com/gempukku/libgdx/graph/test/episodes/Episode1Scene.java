package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class Episode1Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();

    @Override
    public void initializeScene() {
        pipelineRenderer = loadPipelineRenderer();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {

    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("episodes/episode1.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Background Color", Color.ORANGE);
        return pipelineRenderer;
    }
}
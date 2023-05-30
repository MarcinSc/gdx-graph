package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class Episode1Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private PipelineRendererConfiguration configuration;

    @Override
    public String getName() {
        return "YouTube Episode 1";
    }

    @Override
    public void initializeScene() {
        pipelineRenderer = loadPipelineRenderer();
    }

    @Override
    public void renderScene() {
        timeKeeper.updateTime(Gdx.graphics.getDeltaTime());

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {

    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
        configuration.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        configuration = new PipelineRendererConfiguration(timeKeeper);
        configuration.getPipelinePropertyContainer().setValue("Background Color", Color.ORANGE);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode1.json"), configuration);
    }
}
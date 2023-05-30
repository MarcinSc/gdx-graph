package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public interface PreparedRenderingPipeline extends Disposable, PipelinePropertySource {
    void initialize(PipelineRendererConfiguration configuration);

    void startFrame();

    RenderPipeline execute(PipelineRenderingContext pipelineRenderingContext);

    void endFrame();
}

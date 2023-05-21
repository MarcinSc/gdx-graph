package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;

public interface PreparedRenderingPipeline extends Disposable {
    void initialize(PipelineDataProvider pipelineDataProvider);

    void startFrame();

    RenderPipeline execute(PipelineRenderingContext pipelineRenderingContext);

    void endFrame();
}

package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, FullScreenRender fullScreenRender);
}

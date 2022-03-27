package com.gempukku.libgdx.graph.plugin.callback;

import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public interface RenderCallback {
    void renderCallback(RenderPipeline renderPipeline,
                        PipelineDataProvider pipelineDataProvider,
                        PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback);
}

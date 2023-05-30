package com.gempukku.libgdx.graph.render.callback;

import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public interface RenderCallback {
    void renderCallback(RenderPipeline renderPipeline,
                        PipelineRendererConfiguration pipelineRendererConfiguration,
                        PipelineRenderingContext pipelineRenderingContext, PipelineNode.PipelineRequirementsCallback pipelineRequirementsCallback);
}

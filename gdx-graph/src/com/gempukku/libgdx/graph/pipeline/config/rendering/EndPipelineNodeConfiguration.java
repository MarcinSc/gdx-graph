package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class EndPipelineNodeConfiguration extends NodeConfigurationImpl {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end", null);
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
    }
}

package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class EndPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end", null);
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, GraphNodeInputSide.Top, RenderPipeline));
    }
}

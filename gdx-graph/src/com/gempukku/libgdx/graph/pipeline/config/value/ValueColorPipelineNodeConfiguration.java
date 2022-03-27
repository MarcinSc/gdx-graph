package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;

public class ValueColorPipelineNodeConfiguration extends NodeConfigurationImpl {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", Color));
    }
}

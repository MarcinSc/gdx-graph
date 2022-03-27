package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class ValueBooleanPipelineNodeConfiguration extends NodeConfigurationImpl {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", PipelineFieldType.Boolean));
    }
}

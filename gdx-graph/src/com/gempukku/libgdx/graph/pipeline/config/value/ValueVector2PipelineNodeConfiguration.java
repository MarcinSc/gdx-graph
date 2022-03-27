package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;

public class ValueVector2PipelineNodeConfiguration extends NodeConfigurationImpl {
    public ValueVector2PipelineNodeConfiguration() {
        super("ValueVector2", "Vector2", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", Vector2));
    }
}

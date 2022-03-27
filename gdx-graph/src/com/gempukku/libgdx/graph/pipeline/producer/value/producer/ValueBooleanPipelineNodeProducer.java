package com.gempukku.libgdx.graph.pipeline.producer.value.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.node.ValuePipelineNode;

public class ValueBooleanPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ValueBooleanPipelineNodeProducer() {
        super(new ValueBooleanPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        return new ValuePipelineNode(configuration, "value", data.getBoolean("v"),
                new ObjectMap<String, PipelineNode.FieldOutput<?>>());
    }
}

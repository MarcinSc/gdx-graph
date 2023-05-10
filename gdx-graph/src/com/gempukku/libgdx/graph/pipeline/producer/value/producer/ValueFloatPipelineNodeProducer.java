package com.gempukku.libgdx.graph.pipeline.producer.value.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.node.ValuePipelineNode;

public class ValueFloatPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ValueFloatPipelineNodeProducer() {
        super(new ValueFloatPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        return new ValuePipelineNode(configuration, "value", data.getFloat("v1"),
                new ObjectMap<String, PipelineNode.FieldOutput<?>>(), pipelineDataProvider);
    }
}

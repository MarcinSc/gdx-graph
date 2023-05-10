package com.gempukku.libgdx.graph.pipeline.producer.value.producer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector3PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineDataProvider;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.node.ValuePipelineNode;

public class ValueVector3PipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ValueVector3PipelineNodeProducer() {
        super(new ValueVector3PipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes, PipelineDataProvider pipelineDataProvider) {
        return new ValuePipelineNode(configuration, "value", new Vector3(
                data.getFloat("v1"), data.getFloat("v2"), data.getFloat("v3")),
                new ObjectMap<String, PipelineNode.FieldOutput<?>>(), pipelineDataProvider);
    }
}

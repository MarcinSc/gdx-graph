package com.gempukku.libgdx.graph.pipeline.producer.value.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueColorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.node.ValuePipelineNode;

public class ValueColorPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public ValueColorPipelineNodeProducer() {
        super(new ValueColorPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        return new ValuePipelineNode(configuration, "value", Color.valueOf(data.getString("color")),
                new ObjectMap<String, PipelineNode.FieldOutput<?>>());
    }
}

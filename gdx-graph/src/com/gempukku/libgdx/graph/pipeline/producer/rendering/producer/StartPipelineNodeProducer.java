package com.gempukku.libgdx.graph.pipeline.producer.rendering.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.SingleInputsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.node.StartPipelineNode;

public class StartPipelineNodeProducer extends SingleInputsPipelineNodeProducer {
    public StartPipelineNodeProducer() {
        super(new StartPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes) {
        ObjectMap<String, PipelineNode.FieldOutput<?>> outputs = new ObjectMap<>();
        return new StartPipelineNode(outputs);
    }
}

package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public abstract class SingleInputsPipelineNodeProducer extends AbstractPipelineNodeProducer {
    public SingleInputsPipelineNodeProducer(NodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public final PipelineNode createNode(
            JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes,
            PipelineDataProvider pipelineDataProvider) {
        ObjectMap<String, String> singleInputs = new ObjectMap<>();
        for (ObjectMap.Entry<String, Array<String>> entry : inputTypes.entries()) {
            if (entry.value != null && entry.value.size == 1)
                singleInputs.put(entry.key, entry.value.get(0));
        }
        return createNodeForSingleInputs(data, singleInputs, outputTypes, pipelineDataProvider);
    }

    public abstract PipelineNode createNodeForSingleInputs(
            JsonValue data, ObjectMap<String, String> inputTypes, ObjectMap<String, String> outputTypes,
            PipelineDataProvider pipelineDataProvider);
}

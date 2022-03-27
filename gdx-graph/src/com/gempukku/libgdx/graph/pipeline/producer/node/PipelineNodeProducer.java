package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public interface PipelineNodeProducer {
    String getType();

    NodeConfiguration getConfiguration(JsonValue data);

    ObjectMap<String, String> getOutputTypes(JsonValue data, ObjectMap<String, Array<String>> inputTypes);

    PipelineNode createNode(JsonValue data, ObjectMap<String, Array<String>> inputTypes, ObjectMap<String, String> outputTypes);
}

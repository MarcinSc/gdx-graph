package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public abstract class AbstractPipelineNodeProducer implements PipelineNodeProducer {
    protected NodeConfiguration configuration;

    public AbstractPipelineNodeProducer(NodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public ObjectMap<String, String> getOutputTypes(JsonValue data, ObjectMap<String, Array<String>> inputTypes) {
        ObjectMap<String, String> result = new ObjectMap<>();
        for (String fieldName : configuration.getNodeOutputs().keys()) {
            result.put(fieldName, determineOutputType(fieldName, inputTypes));
        }

        return result;
    }

    protected String determineOutputType(String name, ObjectMap<String, Array<String>> inputTypes) {
        return configuration.getNodeOutputs().get(name).determineFieldType(inputTypes);
    }
}

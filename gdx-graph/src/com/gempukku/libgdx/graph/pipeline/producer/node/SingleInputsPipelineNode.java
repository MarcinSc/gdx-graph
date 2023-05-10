package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class SingleInputsPipelineNode extends AbstractPipelineNode {
    protected final PipelineDataProvider pipelineDataProvider;
    protected ObjectMap<String, FieldOutput<?>> inputs = new ObjectMap<>();

    public SingleInputsPipelineNode(ObjectMap<String, FieldOutput<?>> outputs, PipelineDataProvider pipelineDataProvider) {
        super(outputs);
        this.pipelineDataProvider = pipelineDataProvider;
    }

    @Override
    public void setInputs(ObjectMap<String, Array<FieldOutput<?>>> inputs) {
        for (ObjectMap.Entry<String, Array<FieldOutput<?>>> entry : inputs.entries()) {
            if (entry.value != null && entry.value.size == 1)
                this.inputs.put(entry.key, entry.value.get(0));
        }
    }

    protected <T> T getInputValue(String key, T defaultValue) {
        FieldOutput<?> result = inputs.get(key);
        if (result != null)
            return (T) result.getValue();
        else
            return defaultValue;
    }
}

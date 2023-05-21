package com.gempukku.libgdx.graph.pipeline.producer.node;

public class DefaultFieldOutput<T> implements PipelineNode.FieldOutput<T> {
    private final String fieldType;
    private T value;

    public DefaultFieldOutput(String fieldType) {
        this.fieldType = fieldType;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String getPropertyType() {
        return fieldType;
    }

    @Override
    public T getValue() {
        return value;
    }
}

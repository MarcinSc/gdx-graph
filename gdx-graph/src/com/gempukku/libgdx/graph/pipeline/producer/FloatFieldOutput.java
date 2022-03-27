package com.gempukku.libgdx.graph.pipeline.producer;

import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class FloatFieldOutput implements PipelineNode.FieldOutput<Float> {
    private final float value;

    public FloatFieldOutput(float value) {
        this.value = value;
    }

    @Override
    public String getPropertyType() {
        return PipelineFieldType.Float;
    }

    @Override
    public Float getValue() {
        return value;
    }
}

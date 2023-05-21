package com.gempukku.libgdx.graph.pipeline.producer;

import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class BooleanFieldOutput implements PipelineNode.FieldOutput<Boolean> {
    private final boolean value;

    public BooleanFieldOutput(boolean value) {
        this.value = value;
    }

    @Override
    public String getPropertyType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}

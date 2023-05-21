package com.gempukku.libgdx.graph.pipeline.field;

import com.gempukku.libgdx.graph.data.FieldType;

public class BooleanFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Boolean;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return getName().equals(((FieldType) obj).getName());
    }
}

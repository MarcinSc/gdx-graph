package com.gempukku.libgdx.graph.pipeline.field;

import com.gempukku.libgdx.graph.data.FieldType;

public class FloatFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Number || value instanceof FloatProvider;
    }

    @Override
    public Object convert(Object value) {
        if (value instanceof Number)
            return ((Number) value).floatValue();
        return ((FloatProvider) value).get();
    }

    @Override
    public String getName() {
        return PipelineFieldType.Float;
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

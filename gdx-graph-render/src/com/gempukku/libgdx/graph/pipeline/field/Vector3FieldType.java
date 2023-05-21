package com.gempukku.libgdx.graph.pipeline.field;

import com.gempukku.libgdx.graph.data.FieldType;

public class Vector3FieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Vector3;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return PipelineFieldType.Vector3;
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

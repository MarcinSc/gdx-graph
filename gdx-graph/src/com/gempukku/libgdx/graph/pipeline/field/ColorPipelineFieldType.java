package com.gempukku.libgdx.graph.pipeline.field;

import com.badlogic.gdx.graphics.Color;

public class ColorPipelineFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Color;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Color";
    }

    @Override
    public boolean isTexture() {
        return false;
    }
}

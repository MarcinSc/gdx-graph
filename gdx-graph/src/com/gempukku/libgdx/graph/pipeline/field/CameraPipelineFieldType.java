package com.gempukku.libgdx.graph.pipeline.field;

public class CameraPipelineFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.graphics.Camera;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Camera";
    }

    @Override
    public boolean isTexture() {
        return false;
    }
}

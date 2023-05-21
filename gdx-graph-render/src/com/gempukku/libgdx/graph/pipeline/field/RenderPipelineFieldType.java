package com.gempukku.libgdx.graph.pipeline.field;

public class RenderPipelineFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.gempukku.libgdx.graph.pipeline.RenderPipeline;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "RenderPipeline";
    }

    @Override
    public boolean isTexture() {
        return false;
    }
}

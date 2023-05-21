package com.gempukku.libgdx.graph.pipeline.field;

import com.badlogic.gdx.utils.ObjectMap;

public class PipelineFieldTypeRegistry {
    private static final ObjectMap<String, PipelineFieldType> pipelineFieldTypes = new ObjectMap<>();

    public static void registerPipelineFieldType(PipelineFieldType fieldType) {
        pipelineFieldTypes.put(fieldType.getName(), fieldType);
    }

    public static PipelineFieldType findPipelineFieldType(String name) {
        return pipelineFieldTypes.get(name);
    }
}

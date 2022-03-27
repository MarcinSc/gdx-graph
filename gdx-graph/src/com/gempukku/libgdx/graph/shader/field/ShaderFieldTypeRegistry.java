package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.ObjectMap;

public class ShaderFieldTypeRegistry {
    private static final ObjectMap<String, ShaderFieldType> shaderFieldTypes = new ObjectMap<>();

    public static void registerShaderFieldType(ShaderFieldType fieldType) {
        shaderFieldTypes.put(fieldType.getName(), fieldType);
    }

    public static ShaderFieldType findShaderFieldType(String name) {
        return shaderFieldTypes.get(name);
    }
}

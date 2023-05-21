package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.Matrix4ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Matrix4ShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new Matrix4ShaderFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        return new DefaultShaderPropertySource(index, name, type, location, type.convertFromJson(data));
    }
}

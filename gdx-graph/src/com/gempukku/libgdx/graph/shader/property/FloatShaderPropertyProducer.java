package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new FloatFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        return new DefaultShaderPropertySource(index, name, type, location, type.convertFromJson(data));
    }
}

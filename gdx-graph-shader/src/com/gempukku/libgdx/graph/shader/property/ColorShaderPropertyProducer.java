package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.Vector4ShaderFieldType;

public class ColorShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new Vector4ShaderFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data,
                                               PropertyLocation location, String attributeFunction, boolean designTime) {
        return new DefaultShaderPropertySource(index, name, type, location, attributeFunction, type.convertFromJson(data));
    }
}

package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public interface GraphShaderPropertyProducer {
    ShaderFieldType getType();

    PropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime);
}

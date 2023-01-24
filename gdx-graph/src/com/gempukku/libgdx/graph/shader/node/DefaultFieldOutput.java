package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;

public class DefaultFieldOutput implements GraphShaderNodeBuilder.FieldOutput {
    private final ShaderFieldType fieldType;
    private final String representation;

    public DefaultFieldOutput(String fieldType, String representation) {
        this(ShaderFieldTypeRegistry.findShaderFieldType(fieldType), representation);
    }

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation) {
        this.fieldType = fieldType;
        this.representation = representation;
    }

    @Override
    public ShaderFieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }

    public String toString() {
        return getRepresentation();
    }
}

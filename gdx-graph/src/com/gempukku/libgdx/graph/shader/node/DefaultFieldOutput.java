package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;

public class DefaultFieldOutput implements GraphShaderNodeBuilder.FieldOutput {
    private final ShaderFieldType fieldType;
    private final String representation;
    private final String samplerRepresentation;

    public DefaultFieldOutput(String fieldType, String representation) {
        this(fieldType, representation, null);
    }

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation) {
        this(fieldType, representation, null);
    }

    public DefaultFieldOutput(String fieldType, String representation, String samplerRepresentation) {
        this(ShaderFieldTypeRegistry.findShaderFieldType(fieldType), representation, samplerRepresentation);
    }

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation, String samplerRepresentation) {
        this.fieldType = fieldType;
        this.representation = representation;
        this.samplerRepresentation = samplerRepresentation;
    }

    @Override
    public ShaderFieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }

    @Override
    public String getSamplerRepresentation() {
        return samplerRepresentation;
    }

    public String toString() {
        return getRepresentation();
    }
}

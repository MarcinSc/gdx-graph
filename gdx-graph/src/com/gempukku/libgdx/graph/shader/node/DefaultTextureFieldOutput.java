package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DefaultTextureFieldOutput extends DefaultFieldOutput implements GraphShaderNodeBuilder.TextureFieldOutput {
    private String samplerRepresentation;

    public DefaultTextureFieldOutput(String fieldType, String representation, String samplerRepresentation) {
        super(fieldType, representation);
        this.samplerRepresentation = samplerRepresentation;
    }

    public DefaultTextureFieldOutput(ShaderFieldType fieldType, String representation, String samplerRepresentation) {
        super(fieldType, representation);
        this.samplerRepresentation = samplerRepresentation;
    }

    @Override
    public String getSamplerRepresentation() {
        return samplerRepresentation;
    }
}

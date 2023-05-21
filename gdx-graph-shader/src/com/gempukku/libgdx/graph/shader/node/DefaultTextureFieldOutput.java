package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DefaultTextureFieldOutput extends DefaultFieldOutput implements GraphShaderNodeBuilder.TextureFieldOutput {
    private String samplerRepresentation;
    private Texture.TextureWrap uWrap;
    private Texture.TextureWrap vWrap;

    public DefaultTextureFieldOutput(String fieldType, String representation, String samplerRepresentation,
                                     Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) {
        super(fieldType, representation);
        this.samplerRepresentation = samplerRepresentation;
        this.uWrap = uWrap;
        this.vWrap = vWrap;
    }

    public DefaultTextureFieldOutput(ShaderFieldType fieldType, String representation, String samplerRepresentation,
                                     Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) {
        super(fieldType, representation);
        this.samplerRepresentation = samplerRepresentation;
        this.uWrap = uWrap;
        this.vWrap = vWrap;
    }

    @Override
    public String getSamplerRepresentation() {
        return samplerRepresentation;
    }

    @Override
    public Texture.TextureWrap getUWrap() {
        return uWrap;
    }

    @Override
    public Texture.TextureWrap getVWrap() {
        return vWrap;
    }
}

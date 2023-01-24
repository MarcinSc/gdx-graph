package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DefaultTextureShaderPropertySource extends DefaultShaderPropertySource implements TextureShaderPropertySource {
    private Texture.TextureFilter minFilter;
    private Texture.TextureFilter magFilter;

    public DefaultTextureShaderPropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType, PropertyLocation location, Object defaultValue, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
        super(propertyIndex, propertyName, shaderFieldType, location, defaultValue);
        this.minFilter = minFilter;
        this.magFilter = magFilter;
    }

    @Override
    public Texture.TextureFilter getMinFilter() {
        return minFilter;
    }

    @Override
    public Texture.TextureFilter getMagFilter() {
        return magFilter;
    }
}

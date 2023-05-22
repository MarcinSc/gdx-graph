package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DefaultTextureShaderPropertySource extends DefaultShaderPropertySource implements TextureShaderPropertySource {
    private Texture.TextureFilter minFilter;
    private Texture.TextureFilter magFilter;

    public DefaultTextureShaderPropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType,
                                              PropertyLocation location, String attributeFunction, Object defaultValue,
                                              Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
        super(propertyIndex, propertyName, shaderFieldType, location, attributeFunction, defaultValue);
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

    @Override
    public boolean isDefiningAttribute(String attributeName) {
        if (attributeName.equals("a_textureSize_" + getPropertyIndex()))
            return true;
        return super.isDefiningAttribute(attributeName);
    }
}

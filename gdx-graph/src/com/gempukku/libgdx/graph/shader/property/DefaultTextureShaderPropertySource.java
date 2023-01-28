package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.field.Vector2FieldType;
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

    @Override
    public ShaderFieldType getShaderFieldTypeForAttribute(String attributeName) {
        if (attributeName.equals("a_textureSize_" + getPropertyIndex()))
            return new Vector2FieldType();
        return super.getShaderFieldTypeForAttribute(attributeName);
    }

    @Override
    public Object getValueToUseForAttribute(String attributeName, Object givenValue) {
        if (attributeName.equals("a_textureSize_" + getPropertyIndex())) {
            TextureRegion textureRegion = (TextureRegion) getValueToUse(givenValue);
            return new Vector2(textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        }
        return super.getValueToUseForAttribute(attributeName, givenValue);
    }

    @Override
    public boolean isDefiningAttribute(String attributeName) {
        if (attributeName.equals("a_textureSize_" + getPropertyIndex()))
            return true;
        return super.isDefiningAttribute(attributeName);
    }
}

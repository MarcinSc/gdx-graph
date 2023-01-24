package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.TextureRegionShaderFieldType;
import com.gempukku.libgdx.graph.util.WhitePixel;

public abstract class TextureShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new TextureRegionShaderFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        String minFilterStr = data.getString("minFilter", null);
        String magFilterStr = data.getString("magFilter", null);
        Texture.TextureFilter minFilter = (minFilterStr != null) ? Texture.TextureFilter.valueOf(minFilterStr) : Texture.TextureFilter.Nearest;
        Texture.TextureFilter magFilter = (magFilterStr != null) ? Texture.TextureFilter.valueOf(magFilterStr) : Texture.TextureFilter.Nearest;
        if (designTime)
            return new DefaultTextureShaderPropertySource(index, name, type, location, WhitePixel.sharedInstance.textureRegion, minFilter, magFilter);
        else
            return new DefaultTextureShaderPropertySource(index, name, type, location, getDefaultTextureRegion(), minFilter, magFilter);
    }

    protected abstract TextureRegion getDefaultTextureRegion();
}

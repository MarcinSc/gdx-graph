package com.gempukku.libgdx.graph.shader.property;

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

    public PropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        if (designTime)
            return new PropertySource(index, name, type, location, WhitePixel.sharedInstance.textureRegion);
        else
            return new PropertySource(index, name, type, location, getDefaultTextureRegion());
    }

    protected abstract TextureRegion getDefaultTextureRegion();
}

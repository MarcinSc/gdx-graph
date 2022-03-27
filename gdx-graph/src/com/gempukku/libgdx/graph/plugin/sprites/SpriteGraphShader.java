package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class SpriteGraphShader extends GraphShader {
    private Array<String> textureUniformNames;

    public SpriteGraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
        setCulling(BasicShader.Culling.none);
    }

    public Array<String> getTextureUniformNames() {
        if (textureUniformNames == null) {
            textureUniformNames = new Array<>();
            for (PropertySource value : propertySourceMap.values()) {
                if (value.getShaderFieldType().getName().equals(ShaderFieldType.TextureRegion))
                    textureUniformNames.add(value.getPropertyName());
            }
        }
        return textureUniformNames;
    }

    public void renderSprites(ShaderContextImpl shaderContext, SpriteData spriteData) {
        spriteData.prepareForRender(shaderContext);
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        spriteData.render(shaderContext, program, getAttributeLocations());
    }
}

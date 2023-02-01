package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;

public class TextureCustomization implements PropertyGraphBoxCustomization {
    @Override
    public void process(ShaderFieldType shaderFieldType, PropertyNodeConfiguration configuration, GraphBoxImpl result, JsonValue data) {
        if (shaderFieldType != null && shaderFieldType.isTexture()) {
            configuration.addNodeOutput(new GraphNodeOutputImpl("textureSize", "Texture Size", ShaderFieldType.Vector2));
            result.addOutputGraphPart(new GraphNodeOutputImpl("textureSize", "Texture Size", ShaderFieldType.Vector2));
            EnumSelectBoxPart<Texture.TextureWrap> uWrap = new EnumSelectBoxPart<>("U wrap ", "uWrap",
                    new StringifyEnum<Texture.TextureWrap>(), Texture.TextureWrap.values());
            EnumSelectBoxPart<Texture.TextureWrap> vWrap = new EnumSelectBoxPart<>("V wrap ", "vWrap",
                    new StringifyEnum<Texture.TextureWrap>(), Texture.TextureWrap.values());
            result.addGraphBoxPart(uWrap);
            result.addGraphBoxPart(vWrap);

            if (data != null) {
                uWrap.initialize(data);
                vWrap.initialize(data);
            }
        }
    }
}

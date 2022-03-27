package com.gempukku.libgdx.graph.plugin.sprites.design;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class SpriteShaderGraphType extends GraphType {
    public static SpriteShaderGraphType instance = new SpriteShaderGraphType();

    public SpriteShaderGraphType() {
        super("Sprite_Shader", true);
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return new PropertyLocation[]{PropertyLocation.Global_Uniform, PropertyLocation.Attribute};
    }
}

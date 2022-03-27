package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class ShadowShaderGraphType extends GraphType {
    public static ShadowShaderGraphType instance = new ShadowShaderGraphType();
    private static PropertyLocation[] propertyLocations = new PropertyLocation[]{PropertyLocation.Uniform, PropertyLocation.Global_Uniform, PropertyLocation.Attribute};

    public ShadowShaderGraphType() {
        super("Shadow_Shader", true);
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return propertyLocations;
    }
}

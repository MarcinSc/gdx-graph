package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class ModelShaderGraphType extends GraphType {
    public static ModelShaderGraphType instance = new ModelShaderGraphType();
    private static PropertyLocation[] propertyLocations = new PropertyLocation[]{PropertyLocation.Uniform, PropertyLocation.Global_Uniform, PropertyLocation.Attribute};

    public ModelShaderGraphType() {
        super("Model_Shader", true);
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return propertyLocations;
    }
}

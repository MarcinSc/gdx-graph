package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class ScreenShaderGraphType extends GraphType {
    public static ScreenShaderGraphType instance = new ScreenShaderGraphType();

    public ScreenShaderGraphType() {
        super("Screen_Shader", true);
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return new PropertyLocation[]{PropertyLocation.Global_Uniform};
    }
}

package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ScreenShaderBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ScreenShaderBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Screen_Shader");
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

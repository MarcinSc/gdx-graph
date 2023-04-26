package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ScreenShaderEditorPart extends ShaderGraphEditorPart {
    private UIGraphType graphType;

    public ScreenShaderEditorPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ScreenShaderGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

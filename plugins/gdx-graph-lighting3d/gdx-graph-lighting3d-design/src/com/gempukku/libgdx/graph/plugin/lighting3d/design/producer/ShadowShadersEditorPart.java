package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ShadowShadersEditorPart extends ShaderGraphEditorPart {
    private UIGraphType graphType;

    public ShadowShadersEditorPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ShadowShaderGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

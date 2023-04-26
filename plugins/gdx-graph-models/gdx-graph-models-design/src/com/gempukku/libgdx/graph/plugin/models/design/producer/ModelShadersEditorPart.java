package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ModelShadersEditorPart extends ShaderGraphEditorPart {
    private UIGraphType graphType;

    public ModelShadersEditorPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

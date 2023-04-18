package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ModelShadersBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ModelShadersBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

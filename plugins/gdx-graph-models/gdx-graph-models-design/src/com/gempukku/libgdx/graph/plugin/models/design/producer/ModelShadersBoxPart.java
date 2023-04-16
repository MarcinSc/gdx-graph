package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsTemplateRegistry;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;

public class ModelShadersBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ModelShadersBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Model_Shader");
    }

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ModelsTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return graphType;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphType.getUIConfigurations();
    }
}

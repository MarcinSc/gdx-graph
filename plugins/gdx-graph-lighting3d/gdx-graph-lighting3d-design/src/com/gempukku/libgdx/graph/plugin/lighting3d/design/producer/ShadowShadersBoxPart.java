package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.ShadowTemplateRegistry;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;

public class ShadowShadersBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ShadowShadersBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Shadow_Shader");
    }

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ShadowTemplateRegistry.getTemplates();
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

package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenTemplateRegistry;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;

public class ScreenShaderBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ScreenShaderBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Screen_Shader");
    }

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ScreenTemplateRegistry.getTemplates();
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

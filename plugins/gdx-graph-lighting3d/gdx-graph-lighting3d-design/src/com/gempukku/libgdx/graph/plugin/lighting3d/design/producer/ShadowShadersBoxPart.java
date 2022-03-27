package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.gempukku.libgdx.graph.plugin.lighting3d.design.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.ShadowTemplateRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.UIShadowShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class ShadowShadersBoxPart extends ShaderGraphBoxPart {
    private static UIGraphConfiguration[] graphConfigurations = new UIGraphConfiguration[]{
            new UIModelShaderConfiguration(),
            new UIShadowShaderConfiguration(),
            new UICommonShaderConfiguration()
    };

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ShadowTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return ShadowShaderGraphType.instance;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}

package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.plugin.screen.design.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenTemplateRegistry;
import com.gempukku.libgdx.graph.plugin.screen.design.UIScreenShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class ScreenShaderBoxPart extends ShaderGraphBoxPart {
    private static UIGraphConfiguration[] graphConfigurations = new UIGraphConfiguration[]{
            new UIScreenShaderConfiguration(),
            new UICommonShaderConfiguration()
    };

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ScreenTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return ScreenShaderGraphType.instance;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}

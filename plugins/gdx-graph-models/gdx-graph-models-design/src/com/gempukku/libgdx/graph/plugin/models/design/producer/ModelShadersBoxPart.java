package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.plugin.models.design.ModelShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsTemplateRegistry;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class ModelShadersBoxPart extends ShaderGraphBoxPart {
    private static UIGraphConfiguration[] graphConfigurations = new UIGraphConfiguration[]{
            new UIModelShaderConfiguration(),
            new UICommonShaderConfiguration()
    };

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ModelsTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return ModelShaderGraphType.instance;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}

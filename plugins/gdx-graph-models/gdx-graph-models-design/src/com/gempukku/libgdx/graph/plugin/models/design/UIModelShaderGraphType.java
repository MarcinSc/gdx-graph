package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIModelShaderGraphType extends ModelShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIModelShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIModelShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "mds";
    }

    @Override
    public String getPresentableName() {
        return "Model shader";
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return ModelsTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

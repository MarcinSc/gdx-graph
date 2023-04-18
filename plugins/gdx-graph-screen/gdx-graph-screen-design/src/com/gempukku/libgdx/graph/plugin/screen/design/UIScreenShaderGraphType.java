package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIScreenShaderGraphType extends ScreenShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIScreenShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIScreenShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "scs";
    }

    @Override
    public String getPresentableName() {
        return "Screen shader";
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return ScreenTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

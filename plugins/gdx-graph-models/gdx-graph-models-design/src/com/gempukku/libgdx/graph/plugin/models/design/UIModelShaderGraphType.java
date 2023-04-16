package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIModelShaderGraphType extends ModelShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIModelShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIModelShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

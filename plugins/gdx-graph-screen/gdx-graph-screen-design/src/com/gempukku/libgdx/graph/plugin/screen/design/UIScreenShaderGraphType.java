package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIScreenShaderGraphType extends ScreenShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIScreenShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIScreenShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.libgdx.graph.plugin.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.design.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIShadowShaderGraphType extends ShadowShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIShadowShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIModelShaderConfiguration(),
                new UIShadowShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

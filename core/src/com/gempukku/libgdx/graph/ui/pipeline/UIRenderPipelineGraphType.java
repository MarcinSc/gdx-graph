package com.gempukku.libgdx.graph.ui.pipeline;

import com.gempukku.libgdx.graph.pipeline.RenderPipelineGraphType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;

public class UIRenderPipelineGraphType extends RenderPipelineGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIRenderPipelineGraphType() {
        configurations = new UIGraphConfiguration[]{new UIPipelineConfiguration()};
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

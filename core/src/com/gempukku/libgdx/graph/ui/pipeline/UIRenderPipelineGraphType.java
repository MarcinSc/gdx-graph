package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class UIRenderPipelineGraphType extends RenderPipelineGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;
    private Drawable icon;

    public UIRenderPipelineGraphType(Drawable icon) {
        this.icon = icon;
        configurations = new UIGraphConfiguration[]{new UIRenderPipelineConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "rnp";
    }

    @Override
    public String getPresentableName() {
        return "Render Pipeline";
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return RenderPipelineTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

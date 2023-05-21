package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class UIScreenShaderGraphType extends ScreenShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;
    private Drawable icon;

    public UIScreenShaderGraphType(Drawable icon) {
        this.icon = icon;
        configurations = new UIGraphConfiguration[]{
                new UIScreenShaderConfiguration(),
                new UIModelShaderConfiguration()};
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
    public Drawable getIcon() {
        return icon;
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

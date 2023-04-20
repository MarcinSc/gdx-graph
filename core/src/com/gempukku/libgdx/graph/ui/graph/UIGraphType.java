package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.GraphType;

public interface UIGraphType extends GraphType {
    String getFileExtension();

    String getPresentableName();

    Drawable getIcon();

    Iterable<? extends GraphTemplate> getGraphTemplates();

    UIGraphConfiguration[] getUIConfigurations();
}

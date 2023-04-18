package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.GraphType;

public interface UIGraphType extends GraphType {
    String getFileExtension();

    String getPresentableName();

    Iterable<? extends GraphTemplate> getGraphTemplates();

    UIGraphConfiguration[] getUIConfigurations();
}

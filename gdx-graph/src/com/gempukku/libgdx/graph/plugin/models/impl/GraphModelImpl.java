package com.gempukku.libgdx.graph.plugin.models.impl;

import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;

public class GraphModelImpl implements GraphModel {
    private final String tag;
    private final RenderableModel renderableModel;

    public GraphModelImpl(String tag, RenderableModel renderableModel) {
        this.tag = tag;
        this.renderableModel = renderableModel;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public RenderableModel getRenderableModel() {
        return renderableModel;
    }
}

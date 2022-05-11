package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.utils.Pool;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;

public class ModelWithTag implements Pool.Poolable {
    private RenderableModel renderableModel;
    private String tag;

    public RenderableModel getRenderableModel() {
        return renderableModel;
    }

    public void setRenderableModel(RenderableModel renderableModel) {
        this.renderableModel = renderableModel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void reset() {
        renderableModel = null;
        tag = null;
    }
}

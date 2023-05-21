package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.utils.Pool;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ModelWithShader implements Pool.Poolable {
    private RenderableModel renderableModel;
    private GraphShader shader;

    public RenderableModel getRenderableModel() {
        return renderableModel;
    }

    public void setRenderableModel(RenderableModel renderableModel) {
        this.renderableModel = renderableModel;
    }

    public GraphShader getShader() {
        return shader;
    }

    public void setShader(GraphShader shader) {
        this.shader = shader;
    }

    @Override
    public void reset() {
        renderableModel = null;
        shader = null;
    }
}

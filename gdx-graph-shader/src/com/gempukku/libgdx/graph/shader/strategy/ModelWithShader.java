package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.utils.Pool;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ModelWithShader implements Pool.Poolable {
    private Object model;
    private GraphShader shader;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public GraphShader getShader() {
        return shader;
    }

    public void setShader(GraphShader shader) {
        this.shader = shader;
    }

    @Override
    public void reset() {
        model = null;
        shader = null;
    }
}

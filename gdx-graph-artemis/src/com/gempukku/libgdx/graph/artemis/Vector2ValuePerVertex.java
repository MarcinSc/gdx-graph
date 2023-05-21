package com.gempukku.libgdx.graph.artemis;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.util.ValuePerVertex;

public class Vector2ValuePerVertex implements ValuePerVertex {
    private static final Vector2 tmp = new Vector2();
    private float[] data;

    public Vector2ValuePerVertex(float[] data) {
        this.data = data;
    }

    @Override
    public Object getValue(int index) {
        tmp.set(data[index * 2 + 0], data[index * 2 + 1]);
        return tmp;
    }
}

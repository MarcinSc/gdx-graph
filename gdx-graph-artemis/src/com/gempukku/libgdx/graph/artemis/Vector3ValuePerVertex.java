package com.gempukku.libgdx.graph.artemis;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.util.ValuePerVertex;

public class Vector3ValuePerVertex implements ValuePerVertex {
    private static final Vector3 tmp = new Vector3();
    private float[] data;

    public Vector3ValuePerVertex(float[] data) {
        this.data = data;
    }

    @Override
    public Object getValue(int index) {
        tmp.set(data[index * 3 + 0], data[index * 3 + 1], data[index * 3 + 2]);
        return tmp;
    }
}

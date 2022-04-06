package com.gempukku.libgdx.graph.plugin.sprites;

import com.gempukku.libgdx.graph.util.ValuePerVertex;

public final class ArrayValuePerVertex implements ValuePerVertex {
    private final Object[] vertexValues;

    public ArrayValuePerVertex(Object... values) {
        vertexValues = values;
    }

    public ArrayValuePerVertex(Object v00, Object v10, Object v01, Object v11) {
        vertexValues = new Object[4];
        vertexValues[0] = v00;
        vertexValues[1] = v10;
        vertexValues[2] = v01;
        vertexValues[3] = v11;
    }

    @Override
    public Object getValue(int index) {
        return vertexValues[index];
    }
}

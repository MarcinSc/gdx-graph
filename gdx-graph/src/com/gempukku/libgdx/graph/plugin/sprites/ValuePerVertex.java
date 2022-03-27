package com.gempukku.libgdx.graph.plugin.sprites;

public final class ValuePerVertex {
    private final Object[] vertexValues;

    public ValuePerVertex(Object... values) {
        vertexValues = values;
    }

    public ValuePerVertex(Object v00, Object v10, Object v01, Object v11) {
        vertexValues = new Object[4];
        vertexValues[0] = v00;
        vertexValues[1] = v10;
        vertexValues[2] = v01;
        vertexValues[3] = v11;
    }

    public Object getValue(int index) {
        return vertexValues[index];
    }
}

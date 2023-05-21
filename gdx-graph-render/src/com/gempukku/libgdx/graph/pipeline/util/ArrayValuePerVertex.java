package com.gempukku.libgdx.graph.pipeline.util;

public final class ArrayValuePerVertex<T extends Object> implements ValuePerVertex {
    private final T[] vertexValues;

    public ArrayValuePerVertex(T... values) {
        vertexValues = values;
    }

    @Override
    public T getValue(int index) {
        return vertexValues[index];
    }
}

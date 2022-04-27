package com.gempukku.libgdx.graph.plugin.sprites;

import com.gempukku.libgdx.graph.util.ValuePerVertex;

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

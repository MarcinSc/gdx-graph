package com.gempukku.libgdx.graph.util;

public interface Producer<T> {
    T create();

    void dispose(T object);
}

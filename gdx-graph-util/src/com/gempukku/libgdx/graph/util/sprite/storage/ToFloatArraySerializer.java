package com.gempukku.libgdx.graph.util.sprite.storage;

public interface ToFloatArraySerializer<T> {
    int getFloatCount();

    void serializeToFloatArray(T value, float[] floatArray, int startIndex);
}

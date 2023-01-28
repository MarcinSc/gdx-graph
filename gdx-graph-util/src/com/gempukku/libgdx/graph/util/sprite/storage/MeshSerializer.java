package com.gempukku.libgdx.graph.util.sprite.storage;

public interface MeshSerializer<T> {
    int getIndexCount(T object);

    int getFloatCount(T object);

    void serializeIndices(T object, short[] indices, int indexStart);

    void serializeVertices(T object, float[] vertexValues, int vertexStart);
}

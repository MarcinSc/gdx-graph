package com.gempukku.libgdx.graph.util.storage;

public interface MeshSerializer<T> {
    int getFloatsPerVertex();

    int getIndexCount(T object);

    int getFloatCount(T object);

    void serializeIndices(T object, short[] indices, int indexStart);

    void serializeVertices(T object, float[] vertexValues, int vertexStart);
}

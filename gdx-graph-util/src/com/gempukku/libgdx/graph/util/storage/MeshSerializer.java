package com.gempukku.libgdx.graph.util.storage;

public interface MeshSerializer<T> {
    int getFloatsPerVertex();

    int getIndexCount(T object);

    int getVertexCount(T object);

    void serializeIndices(T object, short[] indices, int indexStart, int vertexStart);

    void serializeVertices(T object, float[] vertexValues, int vertexStart);
}

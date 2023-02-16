package com.gempukku.libgdx.graph.util.storage;

public interface MeshUpdater {
    void updateIndices(short[] indexValues, int startIndex, int count);

    void updateMeshValues(float[] values, int startIndex, int count);
}

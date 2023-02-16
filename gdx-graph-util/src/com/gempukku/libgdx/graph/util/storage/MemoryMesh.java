package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.Mesh;

public interface MemoryMesh {
    int getMaxVertexCount();

    int getMaxIndexCount();

    void setupGdxMesh(Mesh mesh);

    boolean isEmpty();

    void updateGdxMesh(MeshUpdater meshUpdater);

    void renderGdxMesh(IndexedMeshRenderer meshRenderer);
}

package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;

public interface MemoryMesh {
    int getMaxVertexCount();

    int getMaxIndexCount();

    void setupGdxMesh(Mesh mesh);

    boolean isEmpty();

    void updateGdxMesh(Mesh mesh);

    void renderGdxMesh(ShaderProgram shaderProgram, Mesh mesh, int[] attributeLocations, MeshRenderer meshRenderer);
}

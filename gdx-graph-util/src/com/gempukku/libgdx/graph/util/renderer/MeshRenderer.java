package com.gempukku.libgdx.graph.util.renderer;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface MeshRenderer {
    void renderMesh(ShaderProgram shader, Mesh mesh, int indexStart, int indexCount, int[] locations);
}

package com.gempukku.libgdx.graph.util.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TrianglesMeshRenderer implements MeshRenderer {
    @Override
    public void renderMesh(ShaderProgram shader, Mesh mesh, int indexStart, int indexCount, int[] locations) {
        mesh.bind(shader, locations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexCount, GL20.GL_UNSIGNED_SHORT, indexStart);
        mesh.unbind(shader, locations);
    }
}

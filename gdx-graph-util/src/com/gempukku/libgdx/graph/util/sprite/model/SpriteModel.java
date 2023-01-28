package com.gempukku.libgdx.graph.util.sprite.model;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface SpriteModel {
    int getVertexCount();

    int getIndexCount();

    void initializeIndexBuffer(short[] indexBuffer, int numberOfSprites);

    void renderMesh(ShaderProgram shader, Mesh mesh,
                    int indexStart, int indexCount,
                    int[] locations);
}

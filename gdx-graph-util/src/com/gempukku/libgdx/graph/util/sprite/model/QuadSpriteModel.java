package com.gempukku.libgdx.graph.util.sprite.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class QuadSpriteModel implements SpriteModel {
    private static final int VERTEX_COUNT = 4;
    private static final int INDEX_COUNT = 6;

    @Override
    public int getVertexCount() {
        return VERTEX_COUNT;
    }

    @Override
    public int getIndexCount() {
        return INDEX_COUNT;
    }

    @Override
    public void initializeIndexBuffer(short[] indexBuffer, int numberOfSprites) {
        int vertexIndex = 0;
        for (int i = 0; i < numberOfSprites * INDEX_COUNT; i += INDEX_COUNT) {
            indexBuffer[i + 0] = (short) (vertexIndex * 4 + 0);
            indexBuffer[i + 1] = (short) (vertexIndex * 4 + 1);
            indexBuffer[i + 2] = (short) (vertexIndex * 4 + 2);
            indexBuffer[i + 3] = (short) (vertexIndex * 4 + 2);
            indexBuffer[i + 4] = (short) (vertexIndex * 4 + 1);
            indexBuffer[i + 5] = (short) (vertexIndex * 4 + 3);
            vertexIndex++;
        }
    }

    @Override
    public void renderMesh(ShaderProgram shader, Mesh mesh, int spriteStartIndex, int spriteCount, int[] locations) {
        mesh.bind(shader, locations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, INDEX_COUNT * spriteCount, GL20.GL_UNSIGNED_SHORT, INDEX_COUNT * spriteStartIndex);
        mesh.unbind(shader, locations);
    }
}

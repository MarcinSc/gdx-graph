package com.gempukku.libgdx.graph.artemis.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class DefaultSpriteModel implements SpriteModel {
    private final int vertexCount;
    private final short[] indices;

    public DefaultSpriteModel(int vertexCount, short[] indices) {
        this.vertexCount = vertexCount;
        this.indices = indices;
    }

    @Override
    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public int getIndexCount() {
        return indices.length;
    }

    @Override
    public void initializeIndexBuffer(short[] indexBuffer, int numberOfSprites) {
        int vertexIndex = 0;

        int indexCountPerSprite = indices.length;
        for (int spriteIndex = 0; spriteIndex < numberOfSprites * indexCountPerSprite; spriteIndex += indexCountPerSprite) {
            for (int index = 0; index < indexCountPerSprite; index++) {
                indexBuffer[spriteIndex + index] = (short) (vertexIndex * vertexCount + indices[index]);
            }
            ++vertexIndex;
        }

    }

    @Override
    public void renderMesh(ShaderProgram shader, Mesh mesh, int indexStart, int indexCount, int[] locations) {
        int indexCountPerSprite = indices.length;
        mesh.bind(shader, locations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexCountPerSprite * indexCount, GL20.GL_UNSIGNED_SHORT, indexCountPerSprite * indexStart);
        mesh.unbind(shader, locations);
    }
}

package com.gempukku.libgdx.graph.util.sprite.model;

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
}

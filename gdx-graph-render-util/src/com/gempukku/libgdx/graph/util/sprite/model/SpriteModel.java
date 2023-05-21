package com.gempukku.libgdx.graph.util.sprite.model;

public interface SpriteModel {
    int getVertexCount();

    int getIndexCount();

    void initializeIndexBuffer(short[] indexBuffer, int numberOfSprites);
}

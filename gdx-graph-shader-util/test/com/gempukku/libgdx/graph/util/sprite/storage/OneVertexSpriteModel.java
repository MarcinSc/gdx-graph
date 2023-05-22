package com.gempukku.libgdx.graph.util.sprite.storage;

import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class OneVertexSpriteModel implements SpriteModel {
    @Override
    public int getVertexCount() {
        return 1;
    }

    @Override
    public int getIndexCount() {
        return 1;
    }

    @Override
    public void initializeIndexBuffer(short[] indexBuffer, int numberOfSprites) {

    }
}

package com.gempukku.libgdx.graph.util.sprite.storage;

import com.gempukku.libgdx.graph.util.sprite.SpriteReference;

public interface SpriteStorage<T> {
    float[] getFloatArray();

    SpriteReference addSprite(T object);

    boolean containsSprite(SpriteReference spriteReference);

    void updateSprite(T object, SpriteReference spriteReference);

    boolean removeSprite(SpriteReference spriteReference);

    int getSpriteCapacity();

    int getSpriteCount();

    boolean isAtCapacity();

    int getMinUpdatedIndex();

    int getMaxUpdatedIndex();

    void resetUpdates();
}

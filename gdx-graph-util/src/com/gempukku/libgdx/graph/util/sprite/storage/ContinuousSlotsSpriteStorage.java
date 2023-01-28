package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;

public class ContinuousSlotsSpriteStorage<T> implements SpriteStorage<T> {
    private final int spriteCapacity;
    private final int spriteSize;
    private final SpriteSerializer<T> serializer;
    private final float[] floatArray;
    private final Array<SpriteReference> sprites;
    private final ObjectSet<SpriteReference> spriteSet = new ObjectSet<>();

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public ContinuousSlotsSpriteStorage(int spriteCapacity, SpriteSerializer<T> serializer) {
        this.spriteCapacity = spriteCapacity;
        this.spriteSize = serializer.getFloatCount();
        this.serializer = serializer;
        this.floatArray = new float[spriteCapacity * spriteSize];
        this.sprites = new Array<>();
    }

    @Override
    public int getSpriteCapacity() {
        return spriteCapacity;
    }

    @Override
    public float[] getFloatArray() {
        return floatArray;
    }

    @Override
    public SpriteReference addSprite(T object) {
        if (isAtCapacity())
            throw new GdxRuntimeException("Should not attempt to add more sprites, already at capacity");

        int objectIndex = sprites.size;

        serializer.serializeToFloatArray(object, floatArray, objectIndex * spriteSize);

        SpriteReference result = new SpriteReferenceImpl();
        sprites.add(result);
        spriteSet.add(result);

        markObjectUpdated(objectIndex);

        return result;
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        return spriteSet.contains(spriteReference);
    }

    @Override
    public void updateSprite(T object, SpriteReference spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        serializer.serializeToFloatArray(object, floatArray, spriteIndex * spriteSize);

        markObjectUpdated(spriteIndex);
    }

    private int getSpriteIndex(SpriteReference spriteReference) {
        return sprites.indexOf(spriteReference, true);
    }

    @Override
    public boolean removeSprite(SpriteReference spriteReference) {
        int spriteIndex = getSpriteIndex(spriteReference);
        int spriteCount = sprites.size;

        if (spriteIndex < spriteCount - 1) {
            // Rewire the ids for the last elements to replace the removed one
            sprites.set(spriteIndex, sprites.get(sprites.size - 1));
            // Move the data of the deleted object in the array
            System.arraycopy(floatArray, (spriteCount - 1) * spriteSize, floatArray, spriteIndex * spriteSize, spriteSize);

            markObjectUpdated(spriteIndex);
        } else {
            // There are no more objects after this one
            sprites.removeIndex(spriteIndex);
        }
        spriteSet.remove(spriteReference);
        return true;
    }

    public void clear() {
        sprites.clear();
    }

    private void markObjectUpdated(int objectIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, objectIndex * spriteSize);
        maxUpdatedIndex = Math.max(maxUpdatedIndex, (objectIndex + 1) * spriteSize);
    }

    public int getSpriteCount() {
        return sprites.size;
    }

    public boolean isAtCapacity() {
        return getSpriteCount() == spriteCapacity;
    }

    public int getMinUpdatedIndex() {
        return minUpdatedIndex;
    }

    public int getMaxUpdatedIndex() {
        return Math.min(maxUpdatedIndex, getSpriteCount() * spriteSize);
    }

    public void resetUpdates() {
        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }
}

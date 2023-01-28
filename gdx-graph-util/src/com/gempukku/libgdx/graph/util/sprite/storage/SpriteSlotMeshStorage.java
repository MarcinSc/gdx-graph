package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.util.Producer;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;
import com.gempukku.libgdx.graph.util.storage.ObjectMeshStorage;

public class SpriteSlotMeshStorage<T, U> implements ObjectMeshStorage<T, U> {
    private final int spriteCapacity;
    private final Producer<U> referenceProducer;
    private final int spriteSize;
    private final MeshSerializer<T> serializer;
    private final short[] shortArray;
    private final float[] floatArray;
    private final Array<U> sprites;
    private final ObjectSet<U> spriteSet = new ObjectSet<>();
    private final int indexCountPerSprite;

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public SpriteSlotMeshStorage(int spriteCapacity,
                                 SpriteModel spriteModel, MeshSerializer<T> serializer,
                                 Producer<U> referenceProducer) {
        this.spriteCapacity = spriteCapacity;
        this.referenceProducer = referenceProducer;
        this.spriteSize = spriteModel.getVertexCount() * serializer.getFloatsPerVertex();
        this.indexCountPerSprite = spriteModel.getIndexCount();
        this.floatArray = new float[spriteCapacity * spriteSize];
        this.shortArray = new short[spriteCapacity * indexCountPerSprite];
        this.serializer = serializer;

        spriteModel.initializeIndexBuffer(shortArray, spriteCapacity);

        this.sprites = new Array<>();
    }

    @Override
    public int getMaxVertexCount() {
        return spriteCapacity * spriteSize;
    }

    @Override
    public boolean canStore(T object) {
        return sprites.size < spriteCapacity;
    }

    @Override
    public short[] getIndexArray() {
        return shortArray;
    }

    @Override
    public float[] getVertexArray() {
        return floatArray;
    }

    @Override
    public U addObject(T object) {
        if (canStore())
            throw new GdxRuntimeException("Should not attempt to add more sprites, already at capacity");

        int objectIndex = sprites.size;

        serializer.serializeVertices(object, floatArray, objectIndex * spriteSize);

        U result = referenceProducer.create();
        sprites.add(result);
        spriteSet.add(result);

        markObjectUpdated(objectIndex);

        return result;
    }

    @Override
    public boolean containsObject(U objectReference) {
        return spriteSet.contains(objectReference);
    }

    @Override
    public U updateObject(T object, U objectReference) {
        int spriteIndex = getSpriteIndex(objectReference);
        serializer.serializeVertices(object, floatArray, spriteIndex * spriteSize);

        markObjectUpdated(spriteIndex);

        return objectReference;
    }

    private int getSpriteIndex(U objectReference) {
        return sprites.indexOf(objectReference, true);
    }

    @Override
    public boolean removeObject(U objectReference) {
        int spriteIndex = getSpriteIndex(objectReference);
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
        spriteSet.remove(objectReference);
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

    public boolean canStore() {
        return getSpriteCount() == spriteCapacity;
    }

    @Override
    public int getMinUpdatedVertexArrayIndex() {
        return minUpdatedIndex;
    }

    @Override
    public int getMaxUpdatedVertexArrayIndex() {
        return Math.min(maxUpdatedIndex, getSpriteCount() * spriteSize);
    }

    @Override
    public int getMinUpdatedIndexArrayIndex() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxUpdatedIndexArrayIndex() {
        return -1;
    }

    public void resetUpdates() {
        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }

    @Override
    public int getUsedIndexStart() {
        return 0;
    }

    @Override
    public int getUsedIndexCount() {
        return sprites.size * indexCountPerSprite;
    }
}

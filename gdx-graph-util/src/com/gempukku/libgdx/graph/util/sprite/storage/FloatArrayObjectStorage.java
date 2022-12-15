package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.utils.ObjectIntMap;

public class FloatArrayObjectStorage<T> {
    private final int objectCapacity;
    private final int objectSize;
    private final ToFloatArraySerializer<T> serializer;
    private final float[] floatArray;
    private final int[] objectIds;

    private int objectCount = 0;
    private final ObjectIntMap<Integer> unitLocations = new ObjectIntMap<>();

    private int nextIdentifier = 0;

    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public FloatArrayObjectStorage(int objectCapacity, ToFloatArraySerializer<T> serializer) {
        this.objectCapacity = objectCapacity;
        this.objectSize = serializer.getFloatCount();
        this.serializer = serializer;
        this.floatArray = new float[objectCapacity * objectSize];
        this.objectIds = new int[objectCapacity];
    }

    public int addObject(T object) {
        if (isAtCapacity())
            return -1;

        int objectIndex = objectCount;

        objectCount++;
        serializer.serializeToFloatArray(object, floatArray, objectIndex * objectSize);

        int objectId = nextIdentifier++;
        objectIds[objectIndex] = objectId;
        unitLocations.put(objectId, objectIndex);

        markObjectUpdated(objectIndex);

        return objectId;
    }

    public float[] getFloatArray() {
        return floatArray;
    }

    public boolean updateObject(T object, int objectId) {
        int objectIndex = unitLocations.get(objectId, -1);
        if (objectIndex == -1)
            return false;
        serializer.serializeToFloatArray(object, floatArray, objectIndex * objectSize);

        markObjectUpdated(objectIndex);

        return true;
    }

    public boolean deleteObject(int objectId) {
        int objectIndex = unitLocations.remove(objectId, -1);
        if (objectIndex == -1)
            return false;

        // There are no more objects after this one
        if (objectIndex < objectCount - 1) {
            // Rewire the ids for the last elements to replace the removed one
            objectIds[objectIndex] = objectIds[objectCount - 1];
            unitLocations.put(objectIds[objectIndex], objectIndex);
            // Move the data of the deleted object in the array
            System.arraycopy(floatArray, (objectCount - 1) * objectSize, floatArray, objectIndex * objectSize, objectSize);

            markObjectUpdated(objectIndex);
        }
        objectCount--;
        return true;
    }

    private void markObjectUpdated(int objectIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, objectIndex * objectSize);
        maxUpdatedIndex = Math.max(maxUpdatedIndex, (objectIndex + 1) * objectSize);
    }

    public int getObjectCount() {
        return objectCount;
    }

    public boolean isAtCapacity() {
        return objectCount == objectCapacity;
    }

    public int getMinUpdatedIndex() {
        return minUpdatedIndex;
    }

    public int getMaxUpdatedIndex() {
        return Math.min(maxUpdatedIndex, objectCount * objectSize);
    }

    public void resetUpdates() {
        minUpdatedIndex = Integer.MAX_VALUE;
        maxUpdatedIndex = -1;
    }
}

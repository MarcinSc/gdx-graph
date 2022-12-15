package com.gempukku.libgdx.graph.util.sprite.storage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FloatArrayObjectStorageTest {
    private TwoFloatSerializer serializer = new TwoFloatSerializer();

    @Test
    public void testEmpty() {
        FloatArrayObjectStorage<TwoFloats> floatArrayObjectStorage = new FloatArrayObjectStorage<>(10, serializer);
        assertEquals(20, floatArrayObjectStorage.getFloatArray().length);
        assertEquals(0, floatArrayObjectStorage.getObjectCount());
        assertEquals(Integer.MAX_VALUE, floatArrayObjectStorage.getMinUpdatedIndex());
        assertEquals(-1, floatArrayObjectStorage.getMaxUpdatedIndex());
    }

    @Test
    public void testAddUnit() {
        FloatArrayObjectStorage<TwoFloats> floatArrayObjectStorage = new FloatArrayObjectStorage<>(10, serializer);
        floatArrayObjectStorage.addObject(new TwoFloats(1, 2));
        assertEquals(1, floatArrayObjectStorage.getObjectCount());
        assertEquals(1f, floatArrayObjectStorage.getFloatArray()[0], 0.001f);
        assertEquals(2f, floatArrayObjectStorage.getFloatArray()[1], 0.001f);
        assertEquals(0, floatArrayObjectStorage.getMinUpdatedIndex());
        assertEquals(2, floatArrayObjectStorage.getMaxUpdatedIndex());
    }

    @Test
    public void testAddTwoRemoveFirst() {
        FloatArrayObjectStorage<TwoFloats> floatArrayObjectStorage = new FloatArrayObjectStorage<>(10, serializer);
        int object1 = floatArrayObjectStorage.addObject(new TwoFloats(1, 2));
        int object2 = floatArrayObjectStorage.addObject(new TwoFloats(3, 4));
        assertTrue(floatArrayObjectStorage.deleteObject(object1));
        assertEquals(1, floatArrayObjectStorage.getObjectCount());
        assertEquals(3f, floatArrayObjectStorage.getFloatArray()[0], 0.001f);
        assertEquals(4f, floatArrayObjectStorage.getFloatArray()[1], 0.001f);
        assertEquals(0, floatArrayObjectStorage.getMinUpdatedIndex());
        assertEquals(2, floatArrayObjectStorage.getMaxUpdatedIndex());
    }

    @Test
    public void testAddTwoRemoveSecond() {
        FloatArrayObjectStorage<TwoFloats> floatArrayObjectStorage = new FloatArrayObjectStorage<>(10, serializer);
        int object1 = floatArrayObjectStorage.addObject(new TwoFloats(1, 2));
        int object2 = floatArrayObjectStorage.addObject(new TwoFloats(3, 4));
        assertTrue(floatArrayObjectStorage.deleteObject(object2));
        assertEquals(1, floatArrayObjectStorage.getObjectCount());
        assertEquals(1f, floatArrayObjectStorage.getFloatArray()[0], 0.001f);
        assertEquals(2f, floatArrayObjectStorage.getFloatArray()[1], 0.001f);
        assertEquals(0, floatArrayObjectStorage.getMinUpdatedIndex());
        assertEquals(2, floatArrayObjectStorage.getMaxUpdatedIndex());
    }

    private static class TwoFloats {
        private float float1;
        private float float2;

        public TwoFloats(float float1, float float2) {
            this.float1 = float1;
            this.float2 = float2;
        }

        public float getFloat1() {
            return float1;
        }

        public float getFloat2() {
            return float2;
        }
    }

    private static class TwoFloatSerializer implements ToFloatArraySerializer<TwoFloats> {
        @Override
        public int getFloatCount() {
            return 2;
        }

        @Override
        public void serializeToFloatArray(TwoFloats value, float[] floatArray, int startIndex) {
            floatArray[startIndex] = value.getFloat1();
            floatArray[startIndex + 1] = value.getFloat2();
        }
    }
}
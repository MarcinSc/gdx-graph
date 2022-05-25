package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.FloatArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FloatBinarySearchTest {
    @Test
    public void testWeightSearching2Elements() {
        FloatArray floatArray = new FloatArray(new float[]{0.5f, 1.0f});
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.1f));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.2f));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.3f));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.4f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.5f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.6f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.7f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.8f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.9f));
        assertEquals(-1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 1f));
    }

    @Test
    public void testWeightSearching3Elements() {
        FloatArray floatArray = new FloatArray(new float[]{0.25f, 0.5f, 1.0f});
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.1f));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.2f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.3f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.4f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.5f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.6f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.7f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.8f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.9f));
        assertEquals(-1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 1f));
    }

    @Test
    public void testWeightSearching() {
        FloatArray floatArray = new FloatArray(new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f});
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0));
        assertEquals(0, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.1f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.2f));
        assertEquals(1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.3f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.4f));
        assertEquals(2, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.5f));
        assertEquals(3, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.6f));
        assertEquals(3, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.7f));
        assertEquals(4, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.8f));
        assertEquals(4, FloatBinarySearch.findLargestLessThanIndex(floatArray, 0.9f));
        assertEquals(-1, FloatBinarySearch.findLargestLessThanIndex(floatArray, 1f));
    }
}
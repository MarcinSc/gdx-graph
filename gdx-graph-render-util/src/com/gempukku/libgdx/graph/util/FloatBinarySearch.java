package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.FloatArray;

public class FloatBinarySearch {
    private FloatBinarySearch() {
    }

    public static int findLargestLessThanIndex(FloatArray sortedArray, float value) {
        return findLargestLessThanIndex(sortedArray, value, 0, sortedArray.size);
    }

    public static int findLargestLessThanIndex(FloatArray sortedArray, float value, int indexStart, int size) {
        if (size == 1) {
            if (value >= sortedArray.get(indexStart))
                return -1;
            return indexStart;
        }
        int midPoint = indexStart + (size / 2) - 1;
        float weightAtMidPoint = sortedArray.get(midPoint);
        if (value < weightAtMidPoint)
            return findLargestLessThanIndex(sortedArray, value, indexStart, size / 2);
        else
            return findLargestLessThanIndex(sortedArray, value, indexStart + (size / 2), size - (size / 2));
    }
}

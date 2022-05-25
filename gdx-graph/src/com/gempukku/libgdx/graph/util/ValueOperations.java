package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ValueOperations {
    private ValueOperations() {
    }

    public static void copyVector3IntoArray(Vector3 vector, float[] floatArray, int index) {
        floatArray[index + 0] = vector.x;
        floatArray[index + 1] = vector.y;
        floatArray[index + 2] = vector.z;
    }

    public static void copyVector2IntoArray(Vector2 vector, float[] floatArray, int index) {
        floatArray[index + 0] = vector.x;
        floatArray[index + 1] = vector.y;
    }

    public static void copyMatrix4IntoArray(Matrix4 matrix, float[] floatArray, int index) {
        System.arraycopy(matrix.val, 0, floatArray, index, 16);
    }

    public static void copyColorIntoArray(Color value, float[] floatArray, int index) {
        floatArray[index + 0] = value.r;
        floatArray[index + 1] = value.g;
        floatArray[index + 2] = value.b;
        floatArray[index + 3] = value.a;
    }
}

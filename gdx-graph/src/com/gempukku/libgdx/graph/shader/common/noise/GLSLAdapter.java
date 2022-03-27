package com.gempukku.libgdx.graph.shader.common.noise;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GLSLAdapter {

    public static Vector3 fract(Vector3 v) {
        return new Vector3(fract(v.x), fract(v.y), fract(v.z));
    }

    public static Vector2 fract(Vector2 v) {
        return new Vector2(fract(v.x), fract(v.y));
    }

    public static Vector3 floor(Vector3 v) {
        return new Vector3(MathUtils.floor(v.x), MathUtils.floor(v.y), MathUtils.floor(v.z));
    }

    public static Vector2 floor(Vector2 v) {
        return new Vector2(MathUtils.floor(v.x), MathUtils.floor(v.y));
    }

    public static float fract(float v) {
        return v - MathUtils.floor(v);
    }

    public static Vector3 abs(Vector3 v) {
        return new Vector3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
    }

    public static Vector2 max(Vector2 v1, Vector2 v2) {
        return new Vector2(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
    }

    public static Vector2 min(Vector2 v1, Vector2 v2) {
        return new Vector2(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
    }

    public static Vector2 sin(Vector2 v) {
        return new Vector2(MathUtils.sin(v.x), MathUtils.sin(v.y));
    }

    public static float dot(Vector2 v1, Vector2 v2) {
        return v1.dot(v2);
    }

    public static float dot(Vector3 v1, Vector3 v2) {
        return v1.dot(v2);
    }

    public static Vector3 step(Vector3 edge, Vector3 v) {
        return new Vector3(
                v.x < edge.x ? 0f : 1f,
                v.y < edge.y ? 0f : 1f,
                v.z < edge.z ? 0f : 1f);
    }

}

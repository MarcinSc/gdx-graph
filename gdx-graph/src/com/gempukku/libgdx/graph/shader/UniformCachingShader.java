package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class UniformCachingShader extends BasicShader {
    private final ObjectMap<Integer, Object> uniformCache = new ObjectMap<Integer, Object>();

    public UniformCachingShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
    }

    private boolean shouldSet(int location, Object value) {
        Object oldValue = uniformCache.get(location);
        return oldValue == null || !oldValue.equals(value);
    }

    @Override
    public void setUniform(int location, Matrix4 value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, new Matrix4(value));
        }
    }

    @Override
    public void setUniform(int location, Matrix3 value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, new Matrix3(value));
        }
    }

    @Override
    public void setUniform(int location, Vector3 value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, new Vector3(value));
        }
    }

    @Override
    public void setUniform(int location, Vector2 value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, new Vector2(value));
        }
    }

    @Override
    public void setUniform(int location, Color value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, new Color(value));
        }
    }

    @Override
    public void setUniform(int location, float value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, value);
        }
    }

    @Override
    public void setUniform(int location, float v1, float v2) {
        float[] arr = (float[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2) {
            super.setUniform(location, v1, v2);
            if (arr == null)
                arr = new float[2];
            arr[0] = v1;
            arr[1] = v2;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, float v1, float v2, float v3) {
        float[] arr = (float[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2 || arr[2] != v3) {
            super.setUniform(location, v1, v2, v3);
            if (arr == null)
                arr = new float[3];
            arr[0] = v1;
            arr[1] = v2;
            arr[2] = v3;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, float v1, float v2, float v3, float v4) {
        float[] arr = (float[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2 || arr[2] != v3 || arr[3] != v4) {
            super.setUniform(location, v1, v2, v3, v4);
            if (arr == null)
                arr = new float[4];
            arr[0] = v1;
            arr[1] = v2;
            arr[2] = v3;
            arr[3] = v4;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, int value) {
        if (shouldSet(location, value)) {
            super.setUniform(location, value);
            uniformCache.put(location, value);
        }
    }


    @Override
    public void setUniform(int location, int v1, int v2) {
        int[] arr = (int[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2) {
            super.setUniform(location, v1, v2);
            if (arr == null)
                arr = new int[2];
            arr[0] = v1;
            arr[1] = v2;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, int v1, int v2, int v3) {
        int[] arr = (int[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2 || arr[2] != v3) {
            super.setUniform(location, v1, v2, v3);
            if (arr == null)
                arr = new int[3];
            arr[0] = v1;
            arr[1] = v2;
            arr[2] = v3;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, int v1, int v2, int v3, int v4) {
        int[] arr = (int[]) uniformCache.get(location);
        if (arr == null || arr[0] != v1 || arr[1] != v2 || arr[2] != v3 || arr[3] != v4) {
            super.setUniform(location, v1, v2, v3, v4);
            if (arr == null)
                arr = new int[4];
            arr[0] = v1;
            arr[1] = v2;
            arr[2] = v3;
            arr[3] = v4;
            uniformCache.put(location, arr);
        }
    }

    @Override
    public void setUniform(int location, TextureDescriptor textureDesc) {
        super.setUniform(location, textureDesc);
    }

    @Override
    public void setUniform(int location, GLTexture texture) {
        super.setUniform(location, texture);
    }

    @Override
    public void setUniformMatrix4Array(int location, float[] values) {
        if (shouldSet(location, values)) {
            super.setUniformMatrix4Array(location, values);
            float[] copy = new float[values.length];
            System.arraycopy(values, 0, copy, 0, values.length);
            uniformCache.put(location, copy);
        }
    }

    @Override
    public void setUniformFloatArray(int location, float[] values) {
        if (shouldSet(location, values)) {
            super.setUniformFloatArray(location, values);
            float[] copy = new float[values.length];
            System.arraycopy(values, 0, copy, 0, values.length);
            uniformCache.put(location, copy);
        }
    }

    @Override
    public void setUniformVector2Array(int location, float[] values) {
        if (shouldSet(location, values)) {
            super.setUniformVector2Array(location, values);
            float[] copy = new float[values.length];
            System.arraycopy(values, 0, copy, 0, values.length);
            uniformCache.put(location, copy);
        }
    }

    @Override
    public void setUniformVector3Array(int location, float[] values) {
        if (shouldSet(location, values)) {
            super.setUniformVector3Array(location, values);
            float[] copy = new float[values.length];
            System.arraycopy(values, 0, copy, 0, values.length);
            uniformCache.put(location, copy);
        }
    }
}

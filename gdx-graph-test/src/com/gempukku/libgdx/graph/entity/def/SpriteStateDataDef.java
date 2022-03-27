package com.gempukku.libgdx.graph.entity.def;

public class SpriteStateDataDef {
    private String texture;
    private float u;
    private float v;
    private float u2;
    private float v2;
    private int width;
    private int height;
    private float speed;
    private boolean looping;

    public String getTexture() {
        return texture;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getU2() {
        return u2;
    }

    public float getV2() {
        return v2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isLooping() {
        return looping;
    }
}

package com.gempukku.libgdx.graph.entity.def;

import com.badlogic.gdx.math.Vector2;

public class TiledSpriteDef {
    // Tiled attributes
    private String tileTexture;
    private float u;
    private float v;
    private float u2;
    private float v2;
    private Vector2 tileRepeat;

    public String getTileTexture() {
        return tileTexture;
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

    public Vector2 getTileRepeat() {
        return tileRepeat;
    }
}

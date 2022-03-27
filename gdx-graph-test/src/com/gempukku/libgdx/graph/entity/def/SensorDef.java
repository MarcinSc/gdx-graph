package com.gempukku.libgdx.graph.entity.def;

import com.badlogic.gdx.math.Vector2;

public class SensorDef {
    private String type;
    private Vector2 anchor;
    private Vector2 scale;
    private String[] mask;

    public String getType() {
        return type;
    }

    public Vector2 getAnchor() {
        return anchor;
    }

    public Vector2 getScale() {
        return scale;
    }

    public String[] getMask() {
        return mask;
    }
}

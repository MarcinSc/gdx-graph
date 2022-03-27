package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class AnchorComponent implements Component {
    private float x;
    private float y;

    public Vector2 getAnchor(Vector2 anchor) {
        return anchor.set(x, y);
    }

    public void setAnchor(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

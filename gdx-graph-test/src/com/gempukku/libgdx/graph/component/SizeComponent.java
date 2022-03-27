package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class SizeComponent implements Component {
    private float x;
    private float y;

    public Vector2 getSize(Vector2 position) {
        return position.set(x, y);
    }

    public void setSize(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

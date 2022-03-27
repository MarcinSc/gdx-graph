package com.gempukku.libgdx.graph.component;

import com.badlogic.gdx.math.Vector3;

public class PositionComponent extends DirtyComponent {
    private float x;
    private float y;
    private float z;

    public Vector3 getPosition(Vector3 position) {
        return position.set(x, y, z);
    }

    public float getZ() {
        return z;
    }

    public void setPosition(float x, float y, float z) {
        if (this.x != x || this.y != y || this.z != z) {
            this.x = x;
            this.y = y;
            this.z = z;
            setDirty();
        }
    }
}

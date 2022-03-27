package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.Vector3;

public class PointPositionGenerator implements PositionGenerator {
    private Vector3 point = new Vector3();

    public Vector3 getPoint() {
        return point;
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        location.set(point);
        return location;
    }
}

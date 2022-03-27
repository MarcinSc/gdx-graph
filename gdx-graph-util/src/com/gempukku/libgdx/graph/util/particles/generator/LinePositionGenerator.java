package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class LinePositionGenerator implements PositionGenerator {
    private Vector3 point1 = new Vector3();
    private Vector3 point2 = new Vector3();

    public Vector3 getPoint1() {
        return point1;
    }

    public Vector3 getPoint2() {
        return point2;
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        float value = MathUtils.random();
        location.set(
                point1.x + (point2.x - point1.x) * value,
                point1.y + (point2.y - point1.y) * value,
                point1.z + (point2.z - point1.z) * value);
        return location;
    }
}

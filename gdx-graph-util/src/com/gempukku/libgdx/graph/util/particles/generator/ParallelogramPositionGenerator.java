package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class ParallelogramPositionGenerator implements PositionGenerator {
    private Vector3 origin = new Vector3();
    private Vector3 direction1 = new Vector3(1, 0, 0);
    private Vector3 direction2 = new Vector3(0, 1, 0);

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection1() {
        return direction1;
    }

    public Vector3 getDirection2() {
        return direction2;
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        location.set(origin).mulAdd(direction1, MathUtils.random()).mulAdd(direction2, MathUtils.random());
        return location;
    }
}

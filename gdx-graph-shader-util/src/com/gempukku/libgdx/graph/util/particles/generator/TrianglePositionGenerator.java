package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.Vector3;

public class TrianglePositionGenerator implements PositionGenerator {
    private final Vector3 p1 = new Vector3();
    private final Vector3 p2 = new Vector3();
    private final Vector3 p3 = new Vector3();

    public TrianglePositionGenerator() {
    }

    public TrianglePositionGenerator(Vector3 p1, Vector3 p2, Vector3 p3) {
        this();
        this.p1.set(p1);
        this.p2.set(p2);
        this.p3.set(p3);
    }

    public Vector3 getP1() {
        return p1;
    }

    public Vector3 getP2() {
        return p2;
    }

    public Vector3 getP3() {
        return p3;
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        return RandomPosition.uniformTrianglePosition(
                p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z, location);
    }
}

package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.Vector3;

public class SpherePositionGenerator implements PositionGenerator {
    private Vector3 center = new Vector3();
    private float radius = 1f;

    public Vector3 getCenter() {
        return center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public Vector3 generateLocation(Vector3 location) {
        return RandomPosition.uniformSpherePosition(center.x, center.y, center.z, radius, location);
    }
}

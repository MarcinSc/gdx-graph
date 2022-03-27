package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
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
        // Uniformly distribute the points on sphere using Marsaglia (1972) method from
        // https://mathworld.wolfram.com/SpherePointPicking.html

        while (true) {
            float x1 = MathUtils.random(-1f, 1f);
            float x2 = MathUtils.random(-1f, 1f);
            float x1Square = x1 * x1;
            float x2Square = x2 * x2;
            if (x1Square + x2Square < 1f) {
                float a = (float) Math.sqrt(1 - x1Square - x2Square);
                float x = 2 * x1 * a;
                float y = 2 * x2 * a;
                float z = 1 - 2 * (x1Square + x2Square);

                float r = (float) Math.pow(MathUtils.random(0, radius), 1d / 3d);
                location.set(center.x + r * x, center.y + r * y, center.z + r * z);
                break;
            }
        }
        return location;
    }
}

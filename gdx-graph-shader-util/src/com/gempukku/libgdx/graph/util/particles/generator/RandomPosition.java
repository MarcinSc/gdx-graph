package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class RandomPosition {
    private RandomPosition() {
    }

    public static Vector3 uniformSpherePosition(
            float centerX, float centerY, float centerZ, float radius, Vector3 result) {
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

                float r = (float) Math.pow(MathUtils.random(0, 1f), 1d / 3d) * radius;
                return result.set(centerX + r * x, centerY + r * y, centerZ + r * z);
            }
        }
    }

    public static Vector3 uniformSphereSurfacePosition(
            float centerX, float centerY, float centerZ, float radius, Vector3 result) {
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
                return result.set(centerX + radius * x, centerY + radius * y, centerZ + radius * z);
            }
        }
    }

    public static Vector3 uniformTrianglePosition(
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            Vector3 result) {

        float u = MathUtils.random();
        float v = MathUtils.random();
        if (u + v > 1) {
            u = 1 - u;
            v = 1 - v;
        }

        return findInParallelogram(x1, y1, z1,
                x2 - x1, y2 - y1, z2 - z1,
                x3 - x1, y3 - y1, z3 - z1,
                result, u, v);
    }

    public static Vector3 uniformParallelogramPosition(
            float x, float y, float z,
            float ax, float ay, float az,
            float bx, float by, float bz,
            Vector3 result) {
        float u = MathUtils.random();
        float v = MathUtils.random();

        return findInParallelogram(x, y, z,
                ax, ay, az,
                bx, by, bz,
                result, u, v);
    }

    private static Vector3 findInParallelogram(
            float x, float y, float z,
            float ax, float ay, float az,
            float bx, float by, float bz,
            Vector3 result, float u, float v) {
        return result.set(
                x + ax * u + bx * v,
                y + ay * u + by * v,
                z + az * u + bz * v);
    }

    public static Vector3 uniformLinePosition(
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            Vector3 result) {
        float u = MathUtils.random();
        return result.set(
                x1 + (x2 - x1) * u,
                y1 + (y2 - y1) * u,
                z1 + (z2 - z1) * u);
    }
}

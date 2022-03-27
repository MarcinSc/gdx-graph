package com.gempukku.libgdx.graph.util.culling;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;

public class SphereBoundsTest extends BoundsTest {
    private float radius;

    public SphereBoundsTest(float radius) {
        this.radius = radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected boolean isInBounds(Frustum frustum, Vector3 position) {
        return frustum.sphereInFrustum(position, radius);
    }
}

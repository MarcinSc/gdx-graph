package com.gempukku.libgdx.graph.util.culling;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;

public class BoundingBoxTest extends BoundsTest {
    private Vector3 dimensions = new Vector3();

    public BoundingBoxTest(Vector3 dimensions) {
        this.dimensions.set(dimensions);
    }

    public void setDimensions(Vector3 dimensions) {
        this.dimensions.set(dimensions);
    }

    @Override
    protected boolean isInBounds(Frustum frustum, Vector3 position) {
        return frustum.boundsInFrustum(position, dimensions);
    }
}

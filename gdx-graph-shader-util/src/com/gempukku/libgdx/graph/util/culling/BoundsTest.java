package com.gempukku.libgdx.graph.util.culling;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;

public abstract class BoundsTest implements CullingTest {
    protected abstract boolean isInBounds(Frustum frustum, Vector3 position);

    @Override
    public boolean isCulled(Camera camera, Vector3 position) {
        return !isInBounds(camera.frustum, position);
    }
}

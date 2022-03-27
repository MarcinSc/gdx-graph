package com.gempukku.libgdx.graph.util.culling;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public interface CullingTest {
    boolean isCulled(Camera camera, Vector3 position);
}

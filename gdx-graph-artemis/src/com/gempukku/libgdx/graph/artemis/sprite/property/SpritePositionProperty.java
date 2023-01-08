package com.gempukku.libgdx.graph.artemis.sprite.property;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;

public class SpritePositionProperty implements EvaluableProperty {
    private Matrix4 transform;
    private Vector3 rightVector;
    private Vector3 upVector;

    public Matrix4 getTransform() {
        return transform;
    }

    public Vector3 getRightVector() {
        return rightVector;
    }

    public Vector3 getUpVector() {
        return upVector;
    }
}

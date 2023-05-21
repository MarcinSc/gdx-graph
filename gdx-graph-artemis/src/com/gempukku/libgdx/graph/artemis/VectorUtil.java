package com.gempukku.libgdx.graph.artemis;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.util.ValueOperations;

public class VectorUtil {
    private static Vector3 tempVector3 = new Vector3();

    public static Vector3ValuePerVertex createCenterSpritePosition(
            float width, float height,
            Vector3 unitRightVector, Vector3 unitUpVector,
            Matrix4 transform) {
        return createSideSpritePosition(-width / 2, -height / 2, width, height,
                unitRightVector, unitUpVector, transform);
    }

    public static Vector3ValuePerVertex createSideSpritePosition(
            float x, float y, float width, float height,
            Vector3 unitRightVector, Vector3 unitUpVector,
            Matrix4 transform) {
        float[] positionAttributeFloatArray = new float[4 * 3];

        tempVector3.setZero()
                .mulAdd(unitRightVector, x)
                .mulAdd(unitUpVector, y).prj(transform);
        ValueOperations.copyVector3IntoArray(tempVector3, positionAttributeFloatArray, 0);
        tempVector3.setZero()
                .mulAdd(unitRightVector, x + width)
                .mulAdd(unitUpVector, y).prj(transform);
        ValueOperations.copyVector3IntoArray(tempVector3, positionAttributeFloatArray, 3);
        tempVector3.setZero()
                .mulAdd(unitRightVector, x)
                .mulAdd(unitUpVector, y + height).prj(transform);
        ValueOperations.copyVector3IntoArray(tempVector3, positionAttributeFloatArray, 6);
        tempVector3.setZero()
                .mulAdd(unitRightVector, x + width)
                .mulAdd(unitUpVector, y + height).prj(transform);
        ValueOperations.copyVector3IntoArray(tempVector3, positionAttributeFloatArray, 9);

        return new Vector3ValuePerVertex(positionAttributeFloatArray);
    }
}

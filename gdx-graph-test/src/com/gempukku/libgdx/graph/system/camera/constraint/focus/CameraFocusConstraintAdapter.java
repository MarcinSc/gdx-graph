package com.gempukku.libgdx.graph.system.camera.constraint.focus;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.system.camera.constraint.CameraConstraint;

public class CameraFocusConstraintAdapter {
    public static CameraFocusConstraint of(final CameraConstraint constraint) {
        return new CameraFocusConstraint() {
            @Override
            public void applyConstraint(Camera camera, Vector2 focus, float delta) {
                constraint.applyConstraint(camera, delta);
            }
        };
    }
}

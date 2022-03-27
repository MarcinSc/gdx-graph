package com.gempukku.libgdx.graph.system.camera.constraint.focus;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public interface CameraFocusConstraint {
    void applyConstraint(Camera camera, Vector2 focus, float delta);
}

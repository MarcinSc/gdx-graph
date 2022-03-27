package com.gempukku.libgdx.graph.system.camera.constraint;

import com.badlogic.gdx.graphics.Camera;

public interface CameraConstraint {
    void applyConstraint(Camera camera, float delta);
}

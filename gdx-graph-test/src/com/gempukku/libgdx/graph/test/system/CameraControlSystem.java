package com.gempukku.libgdx.graph.test.system;

import com.artemis.BaseSystem;
import com.gempukku.libgdx.camera2d.Camera2DController;

public class CameraControlSystem extends BaseSystem {
    private Camera2DController cameraController;

    public void setCameraController(Camera2DController cameraController) {
        this.cameraController = cameraController;
    }

    @Override
    protected void processSystem() {
        cameraController.update(world.getDelta());
    }
}

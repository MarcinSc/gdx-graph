package com.gempukku.libgdx.graph.system.camera.constraint.focus;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class LockedToCameraConstraint implements CameraFocusConstraint {
    private final Vector2 anchor = new Vector2();

    public LockedToCameraConstraint(Vector2 anchor) {
        this.anchor.set(anchor);
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        float moveX = camera.viewportWidth * (currentAnchorX - anchor.x);
        float moveY = camera.viewportHeight * (currentAnchorY - anchor.y);
        camera.position.x += moveX;
        camera.position.y += moveY;
        if (moveX != 0 || moveY != 0)
            camera.update();
    }
}

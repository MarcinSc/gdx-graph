package com.gempukku.libgdx.graph.system.camera.constraint.focus;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class FixedToWindowCameraConstraint implements CameraFocusConstraint {
    private final Rectangle windowRectangle;

    private final Vector2 tmpVector = new Vector2();

    public FixedToWindowCameraConstraint(Rectangle windowRectangle) {
        this.windowRectangle = windowRectangle;
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        Vector2 snapChange = getRequiredChangeToRectangle(windowRectangle, tmpVector, currentAnchorX, currentAnchorY);
        float moveX = camera.viewportWidth * snapChange.x;
        float moveY = camera.viewportHeight * snapChange.y;
        camera.position.x += moveX;
        camera.position.y += moveY;
        if (moveX != 0 || moveY != 0)
            camera.update();
    }

    private Vector2 getRequiredChangeToRectangle(Rectangle rectangle, Vector2 tmpVector, float desiredAnchorX, float desiredAnchorY) {
        Vector2 requiredChange = tmpVector.set(0, 0);
        if (desiredAnchorX < rectangle.x) {
            requiredChange.x = desiredAnchorX - rectangle.x;
        } else if (desiredAnchorX > rectangle.x + rectangle.width) {
            requiredChange.x = desiredAnchorX - (rectangle.x + rectangle.width);
        }
        if (desiredAnchorY < rectangle.y) {
            requiredChange.y = desiredAnchorY - rectangle.y;
        } else if (desiredAnchorY > rectangle.y + rectangle.height) {
            requiredChange.y = desiredAnchorY - (rectangle.y + rectangle.height);
        }
        return requiredChange;
    }

}

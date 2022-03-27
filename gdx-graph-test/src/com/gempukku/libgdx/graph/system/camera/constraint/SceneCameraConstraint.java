package com.gempukku.libgdx.graph.system.camera.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SceneCameraConstraint implements CameraConstraint {
    private final Rectangle scene;

    private final Vector2 tmpVector1 = new Vector2();
    private final Vector2 tmpVector2 = new Vector2();

    public SceneCameraConstraint(Rectangle scene) {
        this.scene = scene;
    }

    @Override
    public void applyConstraint(Camera camera, float delta) {
        Vector2 visibleMin = tmpVector1.set(camera.position.x, camera.position.y).add(-camera.viewportWidth / 2f, -camera.viewportHeight / 2f);
        Vector2 visibleMax = tmpVector2.set(camera.position.x, camera.position.y).add(+camera.viewportWidth / 2f, +camera.viewportHeight / 2f);

        float moveX = Math.min(Math.max(0, scene.x - visibleMin.x), scene.x + scene.width - visibleMax.x);
        float moveY = Math.min(Math.max(0, scene.y - visibleMin.y), scene.y + scene.width - visibleMax.y);

        camera.position.x += moveX;
        camera.position.y += moveY;
        if (moveX != 0 || moveY != 0)
            camera.update();
    }
}

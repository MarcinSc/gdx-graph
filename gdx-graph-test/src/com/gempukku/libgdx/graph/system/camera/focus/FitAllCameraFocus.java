package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class FitAllCameraFocus implements CameraFocus {
    private final Rectangle tmpRectangle = new Rectangle();

    public Array<CameraFocus> cameraFocusArray = new Array<>();

    public FitAllCameraFocus(CameraFocus... cameraFoci) {
        cameraFocusArray.addAll(cameraFoci);
    }

    public void addCameraFocus(CameraFocus cameraFocus) {
        cameraFocusArray.add(cameraFocus);
    }

    public void removeCameraFocus(CameraFocus cameraFocus) {
        cameraFocusArray.removeValue(cameraFocus, true);
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        boolean first = true;
        for (CameraFocus cameraFocus : cameraFocusArray) {
            focus = cameraFocus.getFocus(focus);
            if (first) {
                tmpRectangle.set(focus.x, focus.y, 0, 0);
                first = false;
            } else {
                tmpRectangle.merge(focus);
            }
        }
        return tmpRectangle.getCenter(focus);
    }
}

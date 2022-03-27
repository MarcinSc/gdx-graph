package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

public class WeightBasedFocus implements CameraFocus {
    private final ObjectSet<WeightedCameraFocus> focuses = new ObjectSet<>();
    private final Vector2 tmpVector = new Vector2();

    public void addFocus(WeightedCameraFocus focus) {
        focuses.add(focus);
    }

    public void removeFocus(WeightedCameraFocus focus) {
        focuses.remove(focus);
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        focus.set(0, 0);
        float weightSum = 0;
        for (WeightedCameraFocus weightedFocus : focuses) {
            float weight = weightedFocus.getWeight();
            weightedFocus.getFocus(tmpVector);
            weightSum += weight;
            focus.mulAdd(tmpVector, weight);
        }
        if (weightSum > 0) {
            focus.scl(1 / weightSum);
        }

        return focus;
    }
}

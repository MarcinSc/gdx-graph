package com.gempukku.libgdx.graph.test.system.sensor;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorContactListener;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;
import com.gempukku.libgdx.graph.test.system.OutlineSystem;

public class InteractSensorContactListener implements SensorContactListener {
    private OutlineSystem outlineSystem;

    public InteractSensorContactListener(OutlineSystem outlineSystem) {
        this.outlineSystem = outlineSystem;
    }

    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        outlineSystem.setOutline((Entity) other.getUserData(), true);
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        outlineSystem.setOutline((Entity) other.getUserData(), false);
    }
}

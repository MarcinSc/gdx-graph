package com.gempukku.libgdx.graph.test.system.sensor;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorContactListener;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;

public class InteractSensorContactListener implements SensorContactListener {
    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        // TODO set outline
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        // TODO remove outline
    }
}

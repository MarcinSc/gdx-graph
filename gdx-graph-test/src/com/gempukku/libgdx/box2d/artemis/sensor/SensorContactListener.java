package com.gempukku.libgdx.box2d.artemis.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface SensorContactListener {
    void contactBegun(SensorData sensor, Fixture other);

    void contactEnded(SensorData sensor, Fixture other);
}

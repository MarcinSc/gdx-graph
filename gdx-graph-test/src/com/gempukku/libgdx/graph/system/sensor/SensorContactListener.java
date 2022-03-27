package com.gempukku.libgdx.graph.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.graph.entity.SensorData;

public interface SensorContactListener {
    void contactBegun(SensorData sensor, Fixture other);

    void contactEnded(SensorData sensor, Fixture other);
}

package com.gempukku.libgdx.graph.test.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorContactListener;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;

public class FootSensorContactListener implements SensorContactListener {
    private short groundCategoryBit;

    public FootSensorContactListener(short groundCategoryBit) {
        this.groundCategoryBit = groundCategoryBit;
    }

    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == groundCategoryBit) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            if (footSensorData == null) {
                footSensorData = new FootSensorData();
                sensor.setValue(footSensorData);
            }
            footSensorData.setGrounded(true);
        }
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == groundCategoryBit) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            footSensorData.setGrounded(false);
        }
    }
}

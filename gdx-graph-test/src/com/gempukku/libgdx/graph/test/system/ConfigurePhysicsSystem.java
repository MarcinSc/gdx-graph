package com.gempukku.libgdx.graph.test.system;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.gempukku.libgdx.box2d.artemis.PhysicsSystem;
import com.gempukku.libgdx.box2d.artemis.shape.BoxShapeHandler;
import com.gempukku.libgdx.graph.test.system.sensor.FootSensorContactListener;
import com.gempukku.libgdx.graph.test.system.sensor.InteractSensorContactListener;

@Wire(failOnNull = false)
public class ConfigurePhysicsSystem extends BaseSystem {
    private PhysicsSystem physicsSystem;
    @Wire(failOnNull = false)
    private OutlineSystem outlineSystem;

    private final short environmentCategory = 0b0001;
    private final short characterCategory = 0b0010;
    private final short sensorCategory = 0b0100;
    private final short interactiveCategory = 0b1000;

    @Override
    protected void initialize() {
        physicsSystem.addCategory("Environment", environmentCategory);
        physicsSystem.addCategory("Character", characterCategory);
        physicsSystem.addCategory("Sensor", sensorCategory);
        physicsSystem.addCategory("Interactive", interactiveCategory);

        physicsSystem.addShapeHandler("box", new BoxShapeHandler());

        physicsSystem.addSensorContactListener("foot", new FootSensorContactListener(environmentCategory));
        if (outlineSystem != null) {
            physicsSystem.addSensorContactListener("interact", new InteractSensorContactListener(outlineSystem));
        }
    }

    @Override
    protected void processSystem() {

    }
}

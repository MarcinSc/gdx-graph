package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.entity.def.SensorDef;

public class PhysicsComponent implements Component {
    private String type;
    private Vector2 colliderAnchor;
    private Vector2 colliderScale;
    private String[] category;
    private String[] mask;

    private SensorDef[] sensors;

    private Body body;
    private final Array<SensorData> sensorDataArray = new Array<>();

    public String getType() {
        return type;
    }

    public Vector2 getColliderAnchor() {
        return colliderAnchor;
    }

    public Vector2 getColliderScale() {
        return colliderScale;
    }

    public String[] getCategory() {
        return category;
    }

    public String[] getMask() {
        return mask;
    }

    public SensorDef[] getSensors() {
        return sensors;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void addSensor(SensorData sensorData) {
        sensorDataArray.add(sensorData);
    }

    public SensorData getSensorDataOfType(String type) {
        for (SensorData sensorDatum : sensorDataArray) {
            if (sensorDatum.getType().equals(type))
                return sensorDatum;
        }
        return null;
    }
}

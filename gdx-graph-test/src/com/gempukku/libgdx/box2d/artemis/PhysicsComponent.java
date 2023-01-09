package com.gempukku.libgdx.box2d.artemis;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorDef;

public class PhysicsComponent extends Component {
    public enum BodyType {
        Dynamic, Kinematic, Static
    }

    private BodyType type;
    private boolean fixedRotation;
    private boolean bullet;
    private String[] category;
    private String[] mask;

    private String shape;
    private ObjectMap<String, String> shapeData;

    private SensorDef[] sensors;

    private Body body;
    private Array<SensorData> sensorDataArray = new Array<>();

    public BodyType getType() {
        return type;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public boolean isBullet() {
        return bullet;
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

    public String getShape() {
        return shape;
    }

    public ObjectMap<String, String> getShapeData() {
        return shapeData;
    }

    public SensorData getSensorDataOfType(String type) {
        for (SensorData sensorDatum : sensorDataArray) {
            if (sensorDatum.getType().equals(type))
                return sensorDatum;
        }
        return null;
    }
}

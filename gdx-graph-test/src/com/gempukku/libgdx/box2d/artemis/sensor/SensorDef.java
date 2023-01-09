package com.gempukku.libgdx.box2d.artemis.sensor;

import com.badlogic.gdx.utils.ObjectMap;

public class SensorDef {
    private String type;
    private String category;

    private String shape;
    private ObjectMap<String, String> shapeData;

    private String[] mask;

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getShape() {
        return shape;
    }

    public ObjectMap<String, String> getShapeData() {
        return shapeData;
    }

    public String[] getMask() {
        return mask;
    }
}

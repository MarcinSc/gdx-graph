package com.gempukku.libgdx.graph.entity;

public class SensorData {
    private final String type;
    private Object value;

    public SensorData(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}

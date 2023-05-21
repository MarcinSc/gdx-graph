package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.ObjectMap;

public class MapWritablePropertyContainer implements WritablePropertyContainer {
    private final ObjectMap<String, Object> properties = new ObjectMap<>();

    @Override
    public void setValue(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public void remove(String name) {
        properties.remove(name);
    }

    @Override
    public Object getValue(String name) {
        return properties.get(name);
    }

    public void clear() {
        properties.clear();
    }
}

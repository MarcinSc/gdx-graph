package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;

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

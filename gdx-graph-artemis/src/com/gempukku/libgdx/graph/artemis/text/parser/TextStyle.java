package com.gempukku.libgdx.graph.artemis.text.parser;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class TextStyle implements Pool.Poolable {
    private ObjectMap<String, Object> attributes = new ObjectMap<>();

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public TextStyle duplicate() {
        TextStyle result = Pools.obtain(TextStyle.class);
        result.attributes.putAll(attributes);
        return result;
    }

    @Override
    public void reset() {
        attributes.clear();
    }
}

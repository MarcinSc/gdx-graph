package com.gempukku.libgdx.graph.util.property;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;

public class HierarchicalPropertyContainer implements WritablePropertyContainer {
    private PropertyContainer parent;
    private ObjectMap<String, Object> childValues = new ObjectMap<>();

    public HierarchicalPropertyContainer(PropertyContainer parent) {
        this.parent = parent;
    }

    public PropertyContainer getParent() {
        return parent;
    }

    @Override
    public Object getValue(String name) {
        Object result = childValues.get(name);
        if (result != null)
            return result;

        return parent.getValue(name);
    }

    @Override
    public void setValue(String name, Object value) {
        childValues.put(name, value);
    }

    @Override
    public void remove(String name) {
        childValues.remove(name);
    }

    public void clear() {
        childValues.clear();
    }
}

package com.gempukku.libgdx.graph.util.property;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;

public class HierarchicalPropertyContainer implements WritablePropertyContainer {
    private PropertyContainer parent;
    private ObjectMap<String, Object> childValues = new ObjectMap<>();

    public HierarchicalPropertyContainer() {

    }

    public HierarchicalPropertyContainer(PropertyContainer parent) {
        this.parent = parent;
    }

    public PropertyContainer getParent() {
        return parent;
    }

    public void setParent(PropertyContainer parent) {
        this.parent = parent;
    }

    @Override
    public Object getValue(String name) {
        Object result = childValues.get(name);
        if (result != null)
            return result;

        if (parent != null)
            return parent.getValue(name);

        return null;
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

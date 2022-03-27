package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class PropertyNodeConfiguration extends NodeConfigurationImpl {
    private final String name;
    private final String fieldType;

    public PropertyNodeConfiguration(String name, String fieldType) {
        super("Property", name, null);
        this.name = name;
        this.fieldType = fieldType;
        addNodeOutput(new GraphNodeOutputImpl("value", name, fieldType));
    }

    @Override
    public boolean isValid(ObjectMap<String, Array<String>> inputTypes, Iterable<? extends GraphProperty> properties) {
        for (GraphProperty property : properties) {
            if (property.getName().equals(name) && property.getType().equals(fieldType))
                return true;
        }
        return false;
    }
}

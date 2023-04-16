package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public class DefaultGraphProperty implements GraphProperty {
    private String name;
    private String type;
    private PropertyLocation location;
    private JsonValue data;

    public DefaultGraphProperty(String name, String type, PropertyLocation location, JsonValue data) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.data = data;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public PropertyLocation getLocation() {
        return null;
    }

    @Override
    public JsonValue getData() {
        return null;
    }
}

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
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public PropertyLocation getLocation() {
        return location;
    }

    @Override
    public JsonValue getData() {
        return data;
    }
}

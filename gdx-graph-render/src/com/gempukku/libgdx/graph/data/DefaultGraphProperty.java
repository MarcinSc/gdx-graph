package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public class DefaultGraphProperty implements GraphProperty {
    private final String name;
    private final String type;
    private final JsonValue data;

    public DefaultGraphProperty(String name, String type, JsonValue data) {
        this.name = name;
        this.type = type;
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
    public JsonValue getData() {
        return data;
    }
}

package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public interface GraphProperty {
    String getName();

    String getType();

    PropertyLocation getLocation();

    JsonValue getData();
}

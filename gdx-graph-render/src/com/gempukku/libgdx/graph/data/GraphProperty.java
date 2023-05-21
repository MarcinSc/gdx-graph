package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphProperty {
    String getName();

    String getType();

    JsonValue getData();
}

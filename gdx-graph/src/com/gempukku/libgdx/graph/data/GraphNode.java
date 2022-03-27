package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphNode {
    String getId();

    JsonValue getData();

    NodeConfiguration getConfiguration();
}

package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public interface NodeConfiguration {
    String getType();

    String getName();

    String getMenuLocation();

    ObjectMap<String, GraphNodeInput> getNodeInputs();

    ObjectMap<String, GraphNodeOutput> getNodeOutputs();

    boolean isValid(ObjectMap<String, Array<String>> inputTypes, Iterable<? extends GraphProperty> properties);
}

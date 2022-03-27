package com.gempukku.libgdx.graph.loader;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public interface GraphLoaderCallback<T> {
    void start();

    void addPipelineNode(String id, String type, float x, float y, JsonValue data);

    void addPipelineVertex(String fromNode, String fromField, String toNode, String toField);

    void addPipelineProperty(String type, String name, PropertyLocation location, JsonValue data);

    void addNodeGroup(String name, ObjectSet<String> nodeIds);

    T end();
}

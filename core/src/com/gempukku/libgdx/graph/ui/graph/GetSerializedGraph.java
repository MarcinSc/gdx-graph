package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.JsonValue;


public class GetSerializedGraph extends Event {
    private String id;
    private JsonValue graph;

    public GetSerializedGraph(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public JsonValue getGraph() {
        return graph;
    }

    public void setGraph(JsonValue graph) {
        this.graph = graph;
    }
}

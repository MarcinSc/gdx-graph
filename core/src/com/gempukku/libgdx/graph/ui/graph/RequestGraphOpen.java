package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;


public class RequestGraphOpen extends Event {
    private String id;
    private String title;
    private JsonValue jsonObject;
    private GraphType type;

    public RequestGraphOpen(String id, String title, JsonValue jsonObject, GraphType type) {
        this.id = id;
        this.title = title;
        this.jsonObject = jsonObject;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public JsonValue getJsonObject() {
        return jsonObject;
    }

    public GraphType getType() {
        return type;
    }
}

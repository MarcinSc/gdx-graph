package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GraphStatusChangeEvent extends Event {
    public enum Type {
        DEBUG, INFO, WARNING, ERROR
    }

    private Type type;
    private String message;

    public GraphStatusChangeEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}

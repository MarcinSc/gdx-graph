package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;


public class RequestGraphOpen extends Event {
    private final String path;

    public RequestGraphOpen(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

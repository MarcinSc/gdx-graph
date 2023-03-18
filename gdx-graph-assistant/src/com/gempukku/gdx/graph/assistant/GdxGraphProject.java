package com.gempukku.gdx.graph.assistant;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantPluginProject;

public class GdxGraphProject implements AssistantPluginProject {
    public GdxGraphProject() {

    }

    public GdxGraphProject(JsonValue data) {

    }

    @Override
    public boolean isProjectDirty() {
        return false;
    }

    @Override
    public void processUpdate(float v) {

    }

    @Override
    public JsonValue saveProject() {
        return null;
    }

    @Override
    public void markProjectClean() {

    }

    @Override
    public void closeProject() {

    }
}

package com.gempukku.gdx.graph.assistant;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPlugin;
import com.gempukku.gdx.assistant.plugin.AssistantPluginProject;
import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;

public class GdxGraphAssistantPlugin implements AssistantPlugin {
    @Override
    public String getId() {
        return "gdx-graph";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(0, 0, 1);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(AssistantApplication assistantApplication) {
        assistantApplication.addMenu("Graph/Test", "Test",
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Test");
                    }
                });
    }

    @Override
    public void deregisterPlugin() {

    }

    @Override
    public AssistantPluginProject newProjectCreated() {
        return new GdxGraphProject();
    }

    @Override
    public AssistantPluginProject projectOpened(JsonValue projectData) {
        return new GdxGraphProject(projectData);
    }
}

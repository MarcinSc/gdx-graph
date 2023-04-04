package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPlugin;
import com.gempukku.gdx.assistant.plugin.AssistantPluginProject;
import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.kotcrab.vis.ui.util.dialog.Dialogs;

public class WarningPlugin implements AssistantPlugin {
    @Override
    public AssistantPluginProject newProjectCreated() {
        return new AssistantPluginProject() {
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
        };
    }

    @Override
    public AssistantPluginProject projectOpened(JsonValue jsonValue) {
        return newProjectCreated();
    }

    @Override
    public String getId() {
        return "deprecation-warning";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(0, 0, 0);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(AssistantApplication assistantApplication) {
        Dialogs.DetailsDialog dialog = new Dialogs.DetailsDialog(
                "This application should no longer be opened from this project. Please read the details for how to migrate.",
                "Deprecation message",
                "For editing your graphs you should download the gdx-assistant project (link below), which is a program " +
                        "that allows to open multiple different tools for use in your project within the same application.\n\n" +
                        "You can continue using this method, but now you have to create a project from the \"File->New project\" menu - " +
                        "preferably in the root of your game project, and you can import all the graphs using \"Graph->Import graph\" menu.\n\n" +
                        "To access demos in this project, open the \"demoProject.assp\" from the root of this project. Once you do that, " +
                        "you can find all the demo graphs in the \"Graph->Open graph\" menu.\n\n" +
                        "gdx-assistant is available at:\n" +
                        "https://github.com/MarcinSc/gdx-assistant");
        dialog.setWrapDetails(true);
        dialog.setCopyDetailsButtonVisible(false);
        assistantApplication.addWindow(dialog.fadeIn());
    }

    @Override
    public void deregisterPlugin() {

    }
}

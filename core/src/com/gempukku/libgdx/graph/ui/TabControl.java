package com.gempukku.libgdx.graph.ui;

import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.gdx.assistant.plugin.TabManager;

public interface TabControl extends TabManager {
    void openProjectGraph(String path);

    /**
     * Notifies that the argument tab was closed.
     *
     * @param tab
     */
    void tabClosed(AssistantPluginTab tab);
}

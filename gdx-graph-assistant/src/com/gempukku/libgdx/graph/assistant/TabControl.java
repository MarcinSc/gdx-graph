package com.gempukku.libgdx.graph.assistant;

import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.gdx.assistant.plugin.TabManager;

public interface TabControl extends TabManager {
    /**
     * Notifies that the argument tab was closed.
     *
     * @param tab
     */
    void tabClosed(AssistantPluginTab tab);
}

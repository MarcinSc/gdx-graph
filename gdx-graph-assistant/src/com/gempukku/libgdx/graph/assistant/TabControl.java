package com.gempukku.libgdx.graph.assistant;

import com.gempukku.gdx.assistant.plugin.AssistantTab;

public interface TabControl {
    void switchToTab(GraphTab graphTab);

    AssistantTab addTab(String title, GraphTab graphTab);

    void tabClosed(GraphTab graphTab);
}

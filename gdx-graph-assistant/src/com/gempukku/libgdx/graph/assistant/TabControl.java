package com.gempukku.libgdx.graph.assistant;

public interface TabControl {
    void switchToTab(GraphTab graphTab);

    void addTab(String title, GraphTab graphTab);

    void closeTab(GraphTab graphTab);

    void tabClosed(GraphTab graphTab);
}

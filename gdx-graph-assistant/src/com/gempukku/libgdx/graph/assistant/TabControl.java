package com.gempukku.libgdx.graph.assistant;

public interface TabControl {
    /**
     * Requests to switch to the argument tab.
     *
     * @param graphTab
     */
    void switchToTab(GraphTab graphTab);

    /**
     * Adds the argument tab.
     *
     * @param title
     * @param graphTab
     */
    void addTab(String title, GraphTab graphTab);

    boolean isActiveTab(GraphTab graphTab);

    /**
     * Requests that the argument tab be closed.
     *
     * @param graphTab
     */
    void closeTab(GraphTab graphTab);

    /**
     * Notifies that the argument tab was closed.
     *
     * @param graphTab
     */
    void tabClosed(GraphTab graphTab);
}

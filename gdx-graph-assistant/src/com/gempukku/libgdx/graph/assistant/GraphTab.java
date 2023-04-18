package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.gdx.assistant.plugin.StatusManager;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.ui.DirtyHierarchy;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;

public class GraphTab implements AssistantPluginTab, DirtyHierarchy, TabControl {
    private DirtyHierarchy dirtyHierarchy;
    private TabControl tabControl;
    private GraphWithPropertiesEditor graphWithPropertiesEditor;
    private ObjectMap<String, GraphTab> subGraphTabs = new ObjectMap<>();
    private ObjectMap<String, JsonValue> serializedSubGraphs = new ObjectMap<>();

    private boolean dirty = false;

    public GraphTab(Skin skin, DirtyHierarchy dirtyHierarchy, TabControl tabControl, StatusManager statusManager,
                    GraphWithProperties graph) {
        this.dirtyHierarchy = dirtyHierarchy;
        this.tabControl = tabControl;

        graphWithPropertiesEditor = new GraphWithPropertiesEditor(graph, skin, dirtyHierarchy);
        graphWithPropertiesEditor.addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof RequestGraphOpen) {
                            RequestGraphOpen requestGraphOpen = (RequestGraphOpen) event;
                            String graphId = requestGraphOpen.getId();
                            GraphTab subGraphTab = subGraphTabs.get(graphId);
                            if (subGraphTab != null) {
                                tabControl.switchToTab(subGraphTab);
                            } else {

                                JsonValue jsonObject = requestGraphOpen.getJsonObject();
                                if (serializedSubGraphs.containsKey(graphId))
                                    jsonObject = serializedSubGraphs.get(graphId);

                                GraphType graphType = requestGraphOpen.getType();
                                GraphWithProperties subGraph = GraphLoader.loadGraph(graphType.getType(), jsonObject);
                                subGraphTab = new GraphTab(skin, GraphTab.this, GraphTab.this, statusManager, subGraph);
                                tabControl.addTab(requestGraphOpen.getTitle(), subGraphTab);
                                subGraphTabs.put(graphId, subGraphTab);
                                tabControl.switchToTab(subGraphTab);
                            }
                            return true;
                        } else if (event instanceof GraphRemoved) {
                            GraphRemoved graphRemoved = (GraphRemoved) event;
                            serializedSubGraphs.remove(graphRemoved.getId());
                            GraphTab graphTab = subGraphTabs.get(graphRemoved.getId());
                            if (graphTab != null) {
                                graphTab.forceClose();
                            }
                        } else if (event instanceof GetSerializedGraph) {
                            GetSerializedGraph getSerializedGraph = (GetSerializedGraph) event;
                            getSerializedGraph.setGraph(serializedSubGraphs.get(getSerializedGraph.getId()));
                        } else if (event instanceof GraphChangedEvent) {
                            setDirty();
                        } else if (event instanceof GraphStatusChangeEvent) {
                            statusManager.addStatus(((GraphStatusChangeEvent) event).getMessage());
                        }
                        return false;
                    }
                });
    }

    public Table getContent() {
        return graphWithPropertiesEditor;
    }

    public boolean canGroupNodes() {
        return graphWithPropertiesEditor.canGroupNodes();
    }

    public void createGroup(String groupName) {
        graphWithPropertiesEditor.createGroup(groupName);
    }

    public GraphTab getActiveTab() {
        if (tabControl.isActiveTab(this))
            return this;
        for (GraphTab value : subGraphTabs.values()) {
            GraphTab activeTab = value.getActiveTab();
            if (activeTab != null)
                return activeTab;
        }
        return null;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    public GraphWithProperties getGraph() {
        return graphWithPropertiesEditor.getGraph();
    }

    @Override
    public void switchToTab(GraphTab graphTab) {
        tabControl.switchToTab(graphTab);
    }

    @Override
    public void addTab(String title, GraphTab graphTab) {
        tabControl.addTab(title, graphTab);
    }

    @Override
    public boolean isActiveTab(GraphTab graphTab) {
        return tabControl.isActiveTab(graphTab);
    }

    @Override
    public void closeTab(GraphTab graphTab) {
        tabControl.closeTab(graphTab);
    }

    @Override
    public void tabClosed(GraphTab graphTab) {
        for (ObjectMap.Entry<String, GraphTab> entry : subGraphTabs.entries()) {
            if (entry.value == graphTab) {
                subGraphTabs.remove(entry.key);
                break;
            }
        }
    }

    @Override
    public void setActive(boolean b) {

    }

    @Override
    public void setDirty() {
        dirty = true;
        dirtyHierarchy.setDirty();
    }

    public void markClean() {
        dirty = false;
        for (GraphTab subGraphTab : subGraphTabs.values()) {
            subGraphTab.markClean();
        }
    }

    public void forceClose() {
        tabControl.closeTab(this);
    }

    @Override
    public void closed() {
        graphWithPropertiesEditor.dispose();
        for (GraphTab subGraphTab : subGraphTabs.values()) {
            subGraphTab.forceClose();
        }
        subGraphTabs.clear();
        tabControl.tabClosed(this);
    }
}

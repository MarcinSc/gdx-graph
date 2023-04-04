package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.gdx.assistant.plugin.AssistantTab;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.ui.DirtyHierarchy;
import com.gempukku.libgdx.graph.ui.UIGraphLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.*;

public class GraphTab implements AssistantPluginTab, DirtyHierarchy, TabControl {
    private DirtyHierarchy dirtyHierarchy;
    private TabControl tabControl;
    private GraphDesignTable graphDesignTable;
    private AssistantTab assistantTab;
    private ObjectMap<String, GraphTab> subGraphTabs = new ObjectMap<>();
    private ObjectMap<String, GraphDesignTable> subGraphDesignTables = new ObjectMap<>();
    private ObjectMap<String, JsonValue> serializedSubGraphs = new ObjectMap<>();

    private boolean dirty = false;

    public GraphTab(Skin skin, DirtyHierarchy dirtyHierarchy, TabControl tabControl, GraphDesignTable graphDesignTable) {
        this.dirtyHierarchy = dirtyHierarchy;
        this.tabControl = tabControl;
        this.graphDesignTable = graphDesignTable;
        graphDesignTable.addListener(
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
                                GraphType graphType = requestGraphOpen.getType();
                                GraphDesignTable subGraphDesignTable = new GraphDesignTable(graphType, skin,
                                        GraphTab.this, requestGraphOpen.getGraphConfigurations());
                                JsonValue jsonObject = requestGraphOpen.getJsonObject();
                                if (serializedSubGraphs.containsKey(graphId))
                                    jsonObject = serializedSubGraphs.get(graphId);
                                GraphLoader.loadGraph(jsonObject,
                                        new UIGraphLoaderCallback(
                                                skin, subGraphDesignTable, graphType.getPropertyLocations(),
                                                requestGraphOpen.getGraphConfigurations()),
                                        null);
                                subGraphTab = new GraphTab(skin, GraphTab.this, GraphTab.this, subGraphDesignTable);
                                AssistantTab subAssistantTab = tabControl.addTab(requestGraphOpen.getTitle(), subGraphTab);
                                subGraphTab.setAssistantTab(subAssistantTab);
                                subGraphTabs.put(graphId, subGraphTab);
                                subGraphDesignTables.put(graphId, subGraphDesignTable);
                            }
                            return true;
                        } else if (event instanceof GetSerializedGraph) {
                            GetSerializedGraph getSerializedGraph = (GetSerializedGraph) event;
                            getSerializedGraph.setGraph(serializedSubGraphs.get(getSerializedGraph.getId()));
                        } else if (event instanceof GraphChangedEvent) {
                            setDirty();
                        }
                        return false;
                    }
                });
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    public GraphDesignTable getTable() {
        return graphDesignTable;
    }

    @Override
    public void switchToTab(GraphTab graphTab) {
        tabControl.switchToTab(graphTab);
    }

    @Override
    public AssistantTab addTab(String title, GraphTab graphTab) {
        return tabControl.addTab(title, graphTab);
    }

    @Override
    public void tabClosed(GraphTab graphTab) {
        for (ObjectMap.Entry<String, GraphTab> entry : subGraphTabs.entries()) {
            if (entry.value == graphTab) {
                subGraphTabs.remove(entry.key);
                subGraphDesignTables.remove(entry.key);
                break;
            }
        }
    }

    public void setAssistantTab(AssistantTab assistantTab) {
        this.assistantTab = assistantTab;
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
        assistantTab.closeTab();
        for (GraphTab subGraphTab : subGraphTabs.values()) {
            subGraphTab.forceClose();
        }
        subGraphTabs.clear();
        subGraphDesignTables.clear();
    }

    public JsonValue serializeGraph() {
        for (ObjectMap.Entry<String, GraphDesignTable> entry : subGraphDesignTables.entries()) {
            serializedSubGraphs.put(entry.key, entry.value.serializeGraph());
        }

        return graphDesignTable.serializeGraph();
    }

    @Override
    public void closed() {
        for (GraphTab subGraphTab : subGraphTabs.values()) {
            subGraphTab.forceClose();
        }
        subGraphTabs.clear();
        tabControl.tabClosed(this);
    }
}

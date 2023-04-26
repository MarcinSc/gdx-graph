package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.gdx.assistant.plugin.StatusManager;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.loader.GraphSerializer;
import com.gempukku.libgdx.graph.ui.DirtyHierarchy;
import com.gempukku.libgdx.graph.ui.TabControl;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;

public class GraphTab implements AssistantPluginTab, DirtyHierarchy, TabControl {
    private final DirtyHierarchy dirtyHierarchy;
    private final TabControl tabControl;
    private final GraphWithPropertiesEditor graphWithPropertiesEditor;
    private final ObjectMap<String, AssistantPluginTab> subTabs = new ObjectMap<>();
    private final ObjectMap<String, JsonValue> serializedSubGraphs = new ObjectMap<>();

    private boolean dirty = false;

    public GraphTab(DirtyHierarchy dirtyHierarchy, TabControl tabControl, StatusManager statusManager,
                    GraphWithProperties graph) {
        this.dirtyHierarchy = dirtyHierarchy;
        this.tabControl = tabControl;

        graphWithPropertiesEditor = new GraphWithPropertiesEditor(graph, dirtyHierarchy);
        graphWithPropertiesEditor.addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof RequestGraphOpen) {
                            RequestGraphOpen requestGraphOpen = (RequestGraphOpen) event;
                            String graphId = requestGraphOpen.getId();
                            AssistantPluginTab subGraphTab = subTabs.get(graphId);
                            if (subGraphTab != null) {
                                tabControl.switchToTab(subGraphTab);
                            } else {
                                JsonValue jsonObject = requestGraphOpen.getJsonObject();
                                if (serializedSubGraphs.containsKey(graphId))
                                    jsonObject = serializedSubGraphs.get(graphId);

                                UIGraphType graphType = (UIGraphType) requestGraphOpen.getType();
                                GraphWithProperties subGraph = GraphLoader.loadGraph(graphType.getType(), jsonObject);
                                GraphTab graphTab = new GraphTab(GraphTab.this, GraphTab.this, statusManager, subGraph);
                                tabControl.addTab(requestGraphOpen.getTitle(), graphType.getIcon(), graphTab.getContent(), graphTab);
                                subTabs.put(graphId, graphTab);
                                tabControl.switchToTab(graphTab);
                            }
                            return true;
                        } else if (event instanceof GraphRemoved) {
                            GraphRemoved graphRemoved = (GraphRemoved) event;
                            serializedSubGraphs.remove(graphRemoved.getId());
                            AssistantPluginTab graphTab = subTabs.get(graphRemoved.getId());
                            if (graphTab != null) {
                                tabControl.closeTab(graphTab);
                            }
                            return true;
                        } else if (event instanceof GetSerializedGraph) {
                            GetSerializedGraph getSerializedGraph = (GetSerializedGraph) event;
                            getSerializedGraph.setGraph(serializedSubGraphs.get(getSerializedGraph.getId()));
                            return true;
                        } else if (event instanceof GraphChangedEvent) {
                            setDirty();
                            return true;
                        } else if (event instanceof GraphStatusChangeEvent) {
                            statusManager.addStatus(((GraphStatusChangeEvent) event).getMessage());
                            return true;
                        } else if (event instanceof RequestTabOpen) {
                            RequestTabOpen requestTabOpen = (RequestTabOpen) event;
                            String tabId = requestTabOpen.getId();
                            AssistantPluginTab subTab = subTabs.get(tabId);
                            if (subTab != null) {
                                tabControl.switchToTab(subTab);
                            } else {
                                AssistantPluginTab assistantPluginTab = requestTabOpen.getTabCreator().evaluate(GraphTab.this);
                                tabControl.addTab(requestTabOpen.getTitle(), requestTabOpen.getIcon(), requestTabOpen.getContentSupplier().get(),
                                        assistantPluginTab);
                                subTabs.put(tabId, assistantPluginTab);
                                tabControl.switchToTab(assistantPluginTab);
                            }
                            return true;
                        }
                        return false;
                    }
                });
    }

    public JsonValue serializeGraph() {
        for (ObjectMap.Entry<String, AssistantPluginTab> subTab : subTabs) {
            if (subTab.value instanceof GraphTab) {
                JsonValue jsonValue = ((GraphTab) subTab.value).serializeGraph();
                serializedSubGraphs.put(subTab.key, jsonValue);
            }
        }
        GraphWithProperties graph = graphWithPropertiesEditor.getGraph();
        return GraphSerializer.serializeGraphWithProperties(graph);
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

    @Override
    public boolean isDirty() {
        return dirty;
    }

    public GraphWithProperties getGraph() {
        return graphWithPropertiesEditor.getGraph();
    }

    @Override
    public void addTab(String s, Table table, AssistantPluginTab assistantPluginTab) {
        tabControl.addTab(s, table, assistantPluginTab);
    }

    @Override
    public void addTab(String s, Drawable drawable, Table table, AssistantPluginTab assistantPluginTab) {
        tabControl.addTab(s, drawable, table, assistantPluginTab);
    }

    @Override
    public boolean isActiveTab(AssistantPluginTab assistantPluginTab) {
        return tabControl.isActiveTab(assistantPluginTab);
    }

    @Override
    public AssistantPluginTab getActiveTab() {
        return tabControl.getActiveTab();
    }

    @Override
    public void switchToTab(AssistantPluginTab assistantPluginTab) {
        tabControl.switchToTab(assistantPluginTab);
    }

    @Override
    public void setTabTitle(AssistantPluginTab assistantPluginTab, String s) {
        tabControl.setTabTitle(assistantPluginTab, s);
    }

    @Override
    public void closeTab(AssistantPluginTab assistantPluginTab) {
        tabControl.closeTab(assistantPluginTab);
    }

    @Override
    public void tabClosed(AssistantPluginTab tab) {
        for (ObjectMap.Entry<String, AssistantPluginTab> entry : subTabs.entries()) {
            if (entry.value == tab) {
                subTabs.remove(entry.key);
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
        for (AssistantPluginTab subTab : subTabs.values()) {
            if (subTab instanceof GraphTab) {
                ((GraphTab) subTab).markClean();
            }
        }
    }

    @Override
    public void closed() {
        graphWithPropertiesEditor.dispose();
        for (AssistantPluginTab subGraphTab : subTabs.values()) {
            tabControl.closeTab(subGraphTab);
        }
        subTabs.clear();
        tabControl.tabClosed(this);
    }
}

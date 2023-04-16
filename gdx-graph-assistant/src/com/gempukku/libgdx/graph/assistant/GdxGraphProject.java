package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPluginProject;
import com.gempukku.gdx.assistant.plugin.MenuManager;
import com.gempukku.gdx.assistant.plugin.TabManager;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.assistant.data.GdxGraphData;
import com.gempukku.libgdx.graph.assistant.data.GdxGraphProjectData;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.loader.GraphSerializer;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.ui.DirtyHierarchy;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

public class GdxGraphProject implements AssistantPluginProject, DirtyHierarchy, TabControl {
    private GdxGraphProjectData gdxGraphProjectData;

    private ObjectMap<String, JsonValue> mainGraphs = new ObjectMap<>();
    private ObjectMap<String, GraphTab> mainGraphTabs = new ObjectMap<>();

    private boolean dirty = false;
    private AssistantApplication application;
    private MenuManager menuManager;
    private TabManager tabManager;
    private FileTypeFilter graphFilter;

    private InputValidator graphNameValidator;

    public GdxGraphProject(AssistantApplication application) {
        this(application, null);
    }

    public GdxGraphProject(AssistantApplication application, JsonValue data) {
        this.application = application;
        this.menuManager = application.getMenuManager();
        this.tabManager = application.getTabManager();
        if (data == null) {
            gdxGraphProjectData = new GdxGraphProjectData();
        } else {
            gdxGraphProjectData = new Json().readValue(GdxGraphProjectData.class, data);
        }

        try {
            PluginRegistryImpl.initializePlugins();
        } catch (ReflectionException exp) {
            throw new GdxRuntimeException(exp);
        }

        graphNameValidator = new InputValidator() {
            @Override
            public boolean validateInput(String input) {
                input = input.trim();
                return !input.isEmpty() && !mainGraphs.containsKey(input);
            }
        };

        graphFilter = new FileTypeFilter(true);
        graphFilter.addRule("Render pipeline (*.rnp)", "rnp");
        graphFilter.addRule("Render pipeline [old] (*.json)", "json");

        menuManager.setPopupMenuDisabled("Graph", null, "New", false);
        menuManager.addMenuItem("Graph", "New", "Rendering Pipeline",
                new Runnable() {
                    @Override
                    public void run() {
                        newRenderingPipeline();
                    }
                });

        menuManager.setPopupMenuDisabled("Graph", null, "Open", gdxGraphProjectData.getGraphs().isEmpty());

        menuManager.setMenuItemDisabled("Graph", null, "Import graph", false);
        menuManager.updateMenuItemListener("Graph", null, "Import graph",
                new Runnable() {
                    @Override
                    public void run() {
                        importGraph();
                    }
                });

        JsonReader jsonReader = new JsonReader();
        for (GdxGraphData graph : gdxGraphProjectData.getGraphs()) {
            loadGraphIntoProject(jsonReader, graph);
        }
    }

    private void newRenderingPipeline() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setDirectory(application.getProjectFolder());
        fileChooser.setFileTypeFilter(graphFilter);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(
                new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        FileHandle selectedFile = files.get(0);
                        if (!selectedFile.name().toLowerCase().endsWith(".rnp")) {
                            selectedFile = selectedFile.parent().child(selectedFile.name() + ".rnp");
                        }
                        newRenderingPipelineAtPath(selectedFile);
                    }
                }
        );
        application.addWindow(fileChooser.fadeIn());
    }

    private void newRenderingPipelineAtPath(FileHandle selectedFile) {
        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Choose graph name", "Name the graph", true,
                graphNameValidator,
                new InputDialogAdapter() {
                    @Override
                    public void finished(String input) {
                        String graphId = input.trim();
                        UIGraphType graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Render_Pipeline");
                        GdxGraphData graphData = new GdxGraphData(graphId, graphType.getType(), selectedFile.path());
                        loadGraphIntoProject(new JsonReader(), graphId, graphType, Gdx.files.classpath("template/empty-pipeline.json"));
                        gdxGraphProjectData.getGraphs().add(graphData);
                        setDirty();
                        openGraphTab(graphId, graphType);
                    }
                });
        application.addWindow(inputDialog.fadeIn());
    }

    private void loadGraphIntoProject(JsonReader jsonReader, GdxGraphData graph) {
        UIGraphType graphType = (UIGraphType) GraphTypeRegistry.findGraphType(graph.getGraphType());
        FileHandle graphPath = application.getProjectFolder().child(graph.getPath());
        loadGraphIntoProject(jsonReader, graph.getName(), graphType, graphPath);
    }

    private void loadGraphIntoProject(JsonReader jsonReader, String graphName, UIGraphType graphType, FileHandle graphPath) {
        mainGraphs.put(graphName, jsonReader.parse(graphPath));
        menuManager.addMenuItem("Graph", "Open", graphName,
                new Runnable() {
                    @Override
                    public void run() {
                        openGraphTab(graphName, graphType);
                    }
                });
    }

    private void openGraphTab(String graphId, UIGraphType graphType) {
        GraphTab graphTab = mainGraphTabs.get(graphId);
        if (graphTab != null) {
            tabManager.switchToTab(graphTab);
        } else {
            UIPipelineConfiguration pipelineConfiguration = new UIPipelineConfiguration();
            GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), mainGraphs.get(graphId));
            graphTab = new GraphTab(application.getApplicationSkin(), GdxGraphProject.this, GdxGraphProject.this, application.getStatusManager(), graph, pipelineConfiguration);
            tabManager.addTab(graphId, graphTab.getContent(), graphTab);
            mainGraphTabs.put(graphId, graphTab);
        }
    }

    private void importGraph() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(application.getProjectFolder());
        fileChooser.setFileTypeFilter(graphFilter);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Choose graph name", "Name the graph", true,
                        graphNameValidator,
                        new InputDialogAdapter() {
                            @Override
                            public void finished(String input) {
                                String graphId = input.trim();
                                UIGraphType graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Render_Pipeline");
                                GdxGraphData graphData = new GdxGraphData(graphId, graphType.getType(), selectedFile.path());
                                loadGraphIntoProject(new JsonReader(), graphData);
                                gdxGraphProjectData.getGraphs().add(graphData);
                                setDirty();
                                openGraphTab(graphId, graphType);
                            }
                        });
                application.addWindow(inputDialog.fadeIn());
            }
        });
        application.addWindow(fileChooser.fadeIn());
    }

    @Override
    public void switchToTab(GraphTab graphTab) {
        tabManager.switchToTab(graphTab);
    }

    @Override
    public void addTab(String title, GraphTab graphTab) {
        tabManager.addTab(title, graphTab.getContent(), graphTab);
    }

    @Override
    public void closeTab(GraphTab graphTab) {
        tabManager.closeTab(graphTab);
    }

    @Override
    public void tabClosed(GraphTab graphTab) {
        for (ObjectMap.Entry<String, GraphTab> entry : mainGraphTabs.entries()) {
            if (entry.value == graphTab) {
                mainGraphTabs.remove(entry.key);
                break;
            }
        }
    }

    @Override
    public boolean isProjectDirty() {
        return dirty;
    }

    @Override
    public void processUpdate(float v) {

    }

    @Override
    public JsonValue saveProject() {
        for (ObjectMap.Entry<String, GraphTab> openGraphEntry : mainGraphTabs.entries()) {
            String graphId = openGraphEntry.key;
            GraphWithProperties graph = openGraphEntry.value.getGraph();
            JsonValue serializedGraph = GraphSerializer.serializeGraphWithProperties(graph);
            mainGraphs.put(graphId, serializedGraph);
            GdxGraphData graphData = getGraphDataById(graphId);
            FileHandle graphFile = application.getProjectFolder().child(graphData.getPath());
            graphFile.writeString(serializedGraph.toJson(JsonWriter.OutputType.json), false);
        }

        JsonReader jsonReader = new JsonReader();
        Json json = new Json();
        String jsonString = json.toJson(gdxGraphProjectData, GdxGraphProjectData.class);
        return jsonReader.parse(jsonString);
    }

    private GdxGraphData getGraphDataById(String graphId) {
        for (GdxGraphData graph : gdxGraphProjectData.getGraphs()) {
            if (graph.getName().equals(graphId))
                return graph;
        }
        return null;
    }

    @Override
    public void setDirty() {
        dirty = true;
    }

    @Override
    public void markProjectClean() {
        dirty = false;
        for (GraphTab graphTab : mainGraphTabs.values()) {
            graphTab.markClean();
        }
    }

    @Override
    public void closeProject() {
        for (GraphTab graphTab : mainGraphTabs.values()) {
            graphTab.forceClose();
        }
        mainGraphTabs.clear();
        menuManager.clearPopupMenuContents("Graph", null, "Open");
        menuManager.setPopupMenuDisabled("Graph", null, "Open", true);
        menuManager.setMenuItemDisabled("Graph", null, "Import graph", true);
    }
}

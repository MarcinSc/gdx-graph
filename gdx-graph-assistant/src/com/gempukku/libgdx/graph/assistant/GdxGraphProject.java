package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.gdx.assistant.plugin.*;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.assistant.data.GdxGraphData;
import com.gempukku.libgdx.graph.assistant.data.GdxGraphProjectData;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.ui.TabControl;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;
import com.gempukku.libgdx.ui.graph.validator.GraphValidator;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

public class GdxGraphProject implements AssistantPluginProject, TabControl {
    private GdxGraphProjectData gdxGraphProjectData;

    private ObjectMap<String, JsonValue> mainGraphs = new ObjectMap<>();
    private ObjectMap<String, GraphTab> mainGraphTabs = new ObjectMap<>();

    private AssistantApplication application;
    private MenuManager menuManager;
    private TabManager tabManager;
    private final AssistantProject assistantProject;

    private InputValidator graphNameValidator;

    private boolean dirty;

    public GdxGraphProject(AssistantApplication application, AssistantProject assistantProject) {
        this(application, assistantProject, null);
    }

    public GdxGraphProject(AssistantApplication application, AssistantProject assistantProject, JsonValue data) {
        this.application = application;
        this.assistantProject = assistantProject;
        this.menuManager = application.getMenuManager();
        this.tabManager = application.getTabManager();
        if (data == null) {
            gdxGraphProjectData = new GdxGraphProjectData();
        } else {
            gdxGraphProjectData = new Json().readValue(GdxGraphProjectData.class, data);
        }

        try {
            RuntimePluginRegistry.initializePlugins();
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

        menuManager.setPopupMenuDisabled("Graph", null, "New", false);
        for (GraphType graphType : GraphTypeRegistry.getAllGraphTypes()) {
            if (graphType instanceof UIGraphType) {
                UIGraphType type = (UIGraphType) graphType;
                menuManager.addPopupMenu("Graph", "New", type.getPresentableName());
                menuManager.setPopupMenuDisabled("Graph", "New", type.getPresentableName(),
                        type.getGraphTemplates() == null);
                if (type.getGraphTemplates() != null) {
                    for (GraphTemplate graphTemplate : type.getGraphTemplates()) {
                        menuManager.addMenuItem("Graph", "New" + "/" + type.getPresentableName(), graphTemplate.getTitle(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        newGraph(type, graphTemplate.getGraph());
                                    }
                                });
                    }
                }
            }
        }

        menuManager.setPopupMenuDisabled("Graph", null, "Open", gdxGraphProjectData.getGraphs().isEmpty());
        for (GraphType graphType : GraphTypeRegistry.getAllGraphTypes()) {
            if (graphType instanceof UIGraphType) {
                UIGraphType type = (UIGraphType) graphType;
                menuManager.addPopupMenu("Graph", "Open", type.getPresentableName());
                menuManager.setPopupMenuDisabled("Graph", "Open", type.getPresentableName(), true);
            }
        }

        menuManager.setPopupMenuDisabled("Graph", null, "Import", false);
        for (GraphType graphType : GraphTypeRegistry.getAllGraphTypes()) {
            if (graphType instanceof UIGraphType) {
                UIGraphType type = (UIGraphType) graphType;
                menuManager.addMenuItem("Graph", "Import", type.getPresentableName(),
                        new Runnable() {
                            @Override
                            public void run() {
                                importGraph(type);
                            }
                        });
            }
        }

        menuManager.updateMenuItemListener("Graph", null, "Create group",
                new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Create group",
                                "Specify name of the group", true,
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return input.trim().length() > 0;
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        GraphTab activeTab = getActiveGraphTab();
                                        activeTab.createGroup(input.trim());
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                        application.addWindow(inputDialog.fadeIn());
                    }
                });

        menuManager.updateMenuItemListener("Graph", null, "View shader text",
                new Runnable() {
                    @Override
                    public void run() {
                        GraphTab activeTab = getActiveGraphTab();
                        GraphWithProperties graph = activeTab.getGraph();

                        String type = graph.getType();
                        GraphType graphType = GraphTypeRegistry.findGraphType(type);
                        GraphValidator graphValidator = graphType.getGraphValidator();
                        GraphValidationResult graphValidationResult = graphValidator.validateGraph(graph, graphType.getStartNodeIdForValidation());
                        if (graphValidationResult.hasErrors()) {
                            Dialogs.DetailsDialog errorDialog = new Dialogs.DetailsDialog("Graph has errors, can't generate source code", "Error", null);
                            errorDialog.setModal(true);
                            application.addWindow(errorDialog.fadeIn());
                        } else {
                            GraphShader graphShader = GraphShaderBuilder.buildShader(graph, application.getAssetResolver());
                            ShaderCodeWindow shaderCodeWindow = new ShaderCodeWindow(graphShader);
                            shaderCodeWindow.setCenterOnAdd(true);
                            application.addWindow(shaderCodeWindow.fadeIn());
                        }
                    }
                });

        JsonReader jsonReader = new JsonReader();
        for (GdxGraphData graph : gdxGraphProjectData.getGraphs()) {
            loadGraphIntoProject(jsonReader, graph);
        }
    }

    private FileTypeFilter createGraphFileFilter(UIGraphType graphType, boolean otherExtensionsAllowed) {
        String fileExtension = graphType.getFileExtension();
        FileTypeFilter graphFilter = new FileTypeFilter(otherExtensionsAllowed);
        graphFilter.addRule(graphType.getPresentableName() + " (*." + fileExtension + ")", fileExtension);
        if (otherExtensionsAllowed)
            graphFilter.addRule(graphType.getPresentableName() + " [legacy] (*.json)", "json");
        return graphFilter;
    }

    private void newGraph(UIGraphType graphType, JsonValue graph) {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setDirectory(assistantProject.getAssetFolder());
        fileChooser.setFileTypeFilter(createGraphFileFilter(graphType, false));
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(
                new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> files) {
                        FileHandle selectedFile = files.get(0);
                        String filePath = toProjectChildPath(selectedFile);
                        if (filePath == null) {
                            Dialogs.DetailsDialog error = new Dialogs.DetailsDialog("Can't create a graph outside of the folder the project is in", "Error", null);
                            application.addWindow(error.fadeIn());
                            return;
                        }

                        String fileExtension = graphType.getFileExtension();
                        if (!filePath.toLowerCase().endsWith("." + fileExtension)) {
                            filePath = filePath + "." + fileExtension;
                        }
                        newGraphAtPath(graphType, graph, filePath);
                    }
                }
        );
        application.addWindow(fileChooser.fadeIn());
    }

    private void newGraphAtPath(UIGraphType graphType, JsonValue graph, String filePath) {
        Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Choose graph name", "Name the graph", true,
                graphNameValidator,
                new InputDialogAdapter() {
                    @Override
                    public void finished(String input) {
                        String graphId = input.trim();
                        GdxGraphData graphData = new GdxGraphData(graphId, graphType.getType(), filePath);
                        loadGraphIntoProject(graphId, graphType, graph);
                        gdxGraphProjectData.getGraphs().add(graphData);
                        dirty = true;
                        openGraphTab(graphId, graphType);
                    }
                });
        application.addWindow(inputDialog.fadeIn());
    }

    private void loadGraphIntoProject(JsonReader jsonReader, GdxGraphData graph) {
        UIGraphType graphType = (UIGraphType) GraphTypeRegistry.findGraphType(graph.getGraphType());
        FileHandle graphPath = assistantProject.getAssetFolder().child(graph.getPath());
        loadGraphIntoProject(jsonReader, graph.getName(), graphType, graphPath);
    }

    private void loadGraphIntoProject(JsonReader jsonReader, String graphName, UIGraphType graphType, FileHandle graphPath) {
        loadGraphIntoProject(graphName, graphType, jsonReader.parse(graphPath));
    }

    private void loadGraphIntoProject(String graphName, UIGraphType graphType, JsonValue graph) {
        mainGraphs.put(graphName, graph);
        menuManager.addMenuItem("Graph", "Open/" + graphType.getPresentableName(), graphName,
                new Runnable() {
                    @Override
                    public void run() {
                        openGraphTab(graphName, graphType);
                    }
                });
        menuManager.setPopupMenuDisabled("Graph", "Open", graphType.getPresentableName(), false);
    }

    private String toProjectChildPath(FileHandle file) {
        String projectFolderPath = assistantProject.getAssetFolder().path() + "/";
        if (!file.path().startsWith(projectFolderPath)) {
            return null;
        } else {
            return file.path().substring(projectFolderPath.length());
        }
    }

    private void openGraphTab(String graphId, UIGraphType graphType) {
        GraphTab graphTab = mainGraphTabs.get(graphId);
        if (graphTab != null) {
            tabManager.switchToTab(graphTab);
        } else {
            GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), mainGraphs.get(graphId));
            graphTab = new GraphTab(GdxGraphProject.this, application.getStatusManager(), application.getUndoManager(), graph);
            tabManager.addTab(graphId, graphType.getIcon(), graphTab.getContent(), graphTab);
            mainGraphTabs.put(graphId, graphTab);
        }
    }

    private void importGraph(UIGraphType graphType) {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setDirectory(assistantProject.getAssetFolder());
        fileChooser.setFileTypeFilter(createGraphFileFilter(graphType, true));
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                String filePath = toProjectChildPath(selectedFile);
                if (filePath == null) {
                    Dialogs.DetailsDialog error = new Dialogs.DetailsDialog("Can't create a graph outside of the folder the project is in", "Error", null);
                    application.addWindow(error.fadeIn());
                    return;
                }
                Dialogs.InputDialog inputDialog = new Dialogs.InputDialog("Choose graph name", "Name the graph", true,
                        graphNameValidator,
                        new InputDialogAdapter() {
                            @Override
                            public void finished(String input) {
                                String graphId = input.trim();
                                GdxGraphData graphData = new GdxGraphData(graphId, graphType.getType(), filePath);
                                loadGraphIntoProject(new JsonReader(), graphData);
                                gdxGraphProjectData.getGraphs().add(graphData);
                                dirty = true;
                                openGraphTab(graphId, graphType);
                            }
                        });
                application.addWindow(inputDialog.fadeIn());
            }
        });
        application.addWindow(fileChooser.fadeIn());
    }

    @Override
    public void setTabTitle(AssistantPluginTab tab, String title) {
        tabManager.setTabTitle(tab, title);
    }

    @Override
    public void switchToTab(AssistantPluginTab tab) {
        tabManager.switchToTab(tab);
    }

    @Override
    public void addTab(String title, Table table, AssistantPluginTab tab) {
        tabManager.addTab(title, table, tab);
    }

    @Override
    public void addTab(String title, Drawable icon, Table content, AssistantPluginTab tab) {
        tabManager.addTab(title, icon, content, tab);
    }

    @Override
    public boolean isActiveTab(AssistantPluginTab tab) {
        return tabManager.isActiveTab(tab);
    }

    @Override
    public AssistantPluginTab getActiveTab() {
        return tabManager.getActiveTab();
    }

    @Override
    public void closeTab(AssistantPluginTab tab) {
        tabManager.closeTab(tab);
    }

    @Override
    public void tabClosed(AssistantPluginTab tab) {
        for (ObjectMap.Entry<String, GraphTab> entry : mainGraphTabs.entries()) {
            if (entry.value == tab) {
                mainGraphTabs.remove(entry.key);
                break;
            }
        }
    }

    @Override
    public boolean isProjectDirty() {
        if (dirty)
            return true;
        for (GraphTab value : mainGraphTabs.values()) {
            if (value.isDirty())
                return true;
        }
        return false;
    }

    @Override
    public void processUpdate(float v) {
        // Update menu items
        GraphTab activeGraphTab = getActiveGraphTab();

        boolean groupNodesEnabled = activeGraphTab != null && activeGraphTab.canGroupNodes();
        menuManager.setMenuItemDisabled("Graph", null, "Create group", !groupNodesEnabled);

        boolean canViewShader = activeGraphTab != null && (GraphTypeRegistry.findGraphType(activeGraphTab.getGraph().getType()) instanceof ShaderGraphType);
        menuManager.setMenuItemDisabled("Graph", null, "View shader text", !canViewShader);
    }

    private GraphTab getActiveGraphTab() {
        AssistantPluginTab tab = tabManager.getActiveTab();
        if (tab instanceof GraphTab)
            return (GraphTab) tab;
        return null;
    }

    @Override
    public JsonValue saveProject() {
        for (ObjectMap.Entry<String, GraphTab> openGraphEntry : mainGraphTabs.entries()) {
            String graphId = openGraphEntry.key;
            JsonValue serializedGraph = openGraphEntry.value.serializeGraph();
            mainGraphs.put(graphId, serializedGraph);
            GdxGraphData graphData = getGraphDataById(graphId);
            FileHandle graphFile = assistantProject.getAssetFolder().child(graphData.getPath());
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
    public void markProjectClean() {
        dirty = false;
        for (GraphTab graphTab : mainGraphTabs.values()) {
            graphTab.markClean();
        }
    }

    @Override
    public void closeProject() {
        for (GraphTab graphTab : mainGraphTabs.values()) {
            tabManager.closeTab(graphTab);
        }
        mainGraphTabs.clear();
        menuManager.clearPopupMenuContents("Graph", null, "New");
        menuManager.setPopupMenuDisabled("Graph", null, "New", true);
        menuManager.clearPopupMenuContents("Graph", null, "Open");
        menuManager.setPopupMenuDisabled("Graph", null, "Open", true);
        menuManager.clearPopupMenuContents("Graph", null, "Import");
        menuManager.setPopupMenuDisabled("Graph", null, "Import", true);
        menuManager.setMenuItemDisabled("Graph", null, "Create group", true);
        menuManager.setMenuItemDisabled("Graph", null, "View shader text", true);
    }

    private static class DummyUniformRegistry implements UniformRegistry {
        @Override
        public void registerAttribute(String alias, int componentCount) {

        }

        @Override
        public void registerGlobalUniform(String alias, UniformSetter setter) {

        }

        @Override
        public void registerLocalUniform(String alias, UniformSetter setter) {

        }

        @Override
        public void registerGlobalStructArrayUniform(String alias, String[] fieldNames, StructArrayUniformSetter setter) {

        }

        @Override
        public void registerLocalStructArrayUniform(String alias, String[] fieldNames, StructArrayUniformSetter setter) {

        }
    }
}

package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.kotcrab.vis.ui.util.OsUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LibgdxGraphScreen extends VisTable {
    public static GraphInClipboard graphInClipboard = new GraphInClipboard();
    public static NodesInClipboard nodesInClipboard = new NodesInClipboard();

    private Map<String, JsonValue> savedGraphs = new HashMap<>();
    private FileHandle editedFile;
    private final TabbedPane tabbedPane;
    private GraphDesignTab graphDesignTab;
    private Skin skin;
    private final VisTable insideTable;

    private MenuItem save;
    private MenuItem saveAs;
    private MenuItem exportShader;
    private MenuItem copyShader;
    private MenuItem close;
    private MenuItem createGroup;

    private IntMap<Runnable> shortcuts = new IntMap<>();

    public LibgdxGraphScreen(Skin skin) {
        this.skin = skin;
        setFillParent(true);
        insideTable = new VisTable();
        insideTable.add(new EmptyTabWidget());

        tabbedPane = new TabbedPane();
        tabbedPane.addListener(
                new TabbedPaneAdapter() {
                    @Override
                    public void switchedTab(Tab tab) {
                        insideTable.clearChildren();
                        insideTable.add(tab.getContentTable()).grow().row();

                        save.setDisabled(false);
                        saveAs.setDisabled(false);
                        close.setDisabled(false);
                        createGroup.setDisabled(false);

                        GraphDesignTab designTab = (GraphDesignTab) tab;
                        exportShader.setDisabled(!designTab.getType().isExportable());
                        copyShader.setDisabled(!designTab.getType().isExportable());
                    }

                    @Override
                    public void removedAllTabs() {
                        save.setDisabled(true);
                        saveAs.setDisabled(true);
                        close.setDisabled(true);
                        createGroup.setDisabled(true);
                        exportShader.setDisabled(true);
                        copyShader.setDisabled(true);
                    }
                });

        MenuBar menuBar = createMenuBar();
        add(menuBar.getTable()).growX().row();
        add(tabbedPane.getTable()).left().growX().row();
        add(insideTable).grow().row();

        addListener(
                new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        boolean ctrlPressed = OsUtils.isMac() ?
                                Gdx.input.isKeyPressed(Input.Keys.SYM) :
                                Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
                        if (ctrlPressed) {
                            Runnable runnable = shortcuts.get(keycode);
                            if (runnable != null) {
                                runnable.run();
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    private void addControlShortcut(int key, final MenuItem menuItem, final ChangeListener listener) {
        menuItem.setShortcut(Input.Keys.CONTROL_LEFT, key);
        shortcuts.put(key, new Runnable() {
            @Override
            public void run() {
                if (!menuItem.isDisabled())
                    listener.changed(null, null);
            }
        });
    }

    private void openGraphTab(String id, JsonValue graph, String title, GraphType type, UIGraphConfiguration... configurations) {
        SaveCallback saveCallback = new SaveCallback() {
            @Override
            public void save(GraphDesignTab graphDesignTab) {
                savedGraphs.put(graphDesignTab.getId(), graphDesignTab.serializeGraph());
                graphDesignTab.setDirty(true);
            }
        };

        GraphDesignTab graphDesignTab = new GraphDesignTab(true, type, id, title, skin,
                saveCallback, configurations);
        GraphLoader.loadGraph(graph,
                new UIGraphLoaderCallback(skin, graphDesignTab, type.getPropertyLocations(), configurations), null);
        tabbedPane.add(graphDesignTab);
        tabbedPane.switchTab(graphDesignTab);
        graphDesignTab.setDirty(false);
    }

    private Tab findTabByGraphId(String id) {
        for (Tab tab : tabbedPane.getTabs()) {
            if (tab instanceof GraphDesignTab) {
                GraphDesignTab graphDesignTab = (GraphDesignTab) tab;
                if (graphDesignTab.getId().equals(id))
                    return tab;
            }
        }
        return null;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(createFileMenu());
        menuBar.addMenu(createEditMenu());
        menuBar.addMenu(createGraphMenu());

        return menuBar;
    }

    private Menu createEditMenu() {
        Menu editMenu = new Menu("Edit");

        ChangeListener copyListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                copy();
            }
        };

        ChangeListener pasteListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paste();
            }
        };

        MenuItem copy = new MenuItem("Copy");
        addControlShortcut(Input.Keys.C, copy, copyListener);
        copy.addListener(copyListener);
        editMenu.addItem(copy);

        MenuItem paste = new MenuItem("Paste");
        addControlShortcut(Input.Keys.V, paste, pasteListener);
        paste.addListener(pasteListener);
        editMenu.addItem(paste);

        return editMenu;
    }

    private Menu createGraphMenu() {
        Menu graphMenu = new Menu("Graph");

        ChangeListener createGroupListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createGroup();
            }
        };

        createGroup = new MenuItem("Create group");
        addControlShortcut(Input.Keys.G, createGroup, createGroupListener);
        createGroup.setDisabled(true);
        createGroup.addListener(createGroupListener);
        graphMenu.addItem(createGroup);

        exportShader = new MenuItem("Export shader");
        exportShader.setDisabled(true);
        exportShader.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        exportShader();
                    }
                });
        graphMenu.addItem(exportShader);

        copyShader = new MenuItem("Copy shader");
        copyShader.setDisabled(true);
        copyShader.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        copyShader();
                    }
                });
        graphMenu.addItem(copyShader);

        return graphMenu;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newMenuItem = new MenuItem("New from template");
        newMenuItem.setSubMenu(createTemplateMenu());
        fileMenu.addItem(newMenuItem);

        MenuItem open = new MenuItem("Open");
        open.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        open();
                    }
                });
        fileMenu.addItem(open);

        ChangeListener saveListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save();
            }
        };
        save = new MenuItem("Save");
        save.setDisabled(true);
        addControlShortcut(Input.Keys.S, save, saveListener);
        save.addListener(saveListener);
        fileMenu.addItem(save);

        saveAs = new MenuItem("Save As");
        saveAs.setDisabled(true);
        saveAs.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        saveAs();
                    }
                });
        fileMenu.addItem(saveAs);

        fileMenu.addSeparator();

        close = new MenuItem("Close pipeline");
        close.setDisabled(true);
        close.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        closePipeline();
                    }
                });
        fileMenu.addItem(close);

        fileMenu.addSeparator();

        MenuItem plugins = new MenuItem("Plugins...");
        plugins.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PluginsDialog pluginsDialog = new PluginsDialog();
                        getStage().addActor(pluginsDialog);
                        pluginsDialog.centerWindow();
                    }
                });
        fileMenu.addItem(plugins);

        fileMenu.addSeparator();
        MenuItem exit = new MenuItem("Exit");
        exit.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        closeApplication();
                    }
                });
        fileMenu.addItem(exit);

        return fileMenu;
    }

    private void copy() {
        GraphDesignTab activeTab = (GraphDesignTab) tabbedPane.getActiveTab();
        if (activeTab != null) {
            GraphType type = activeTab.getType();
            GraphContainer graphContainer = activeTab.getGraphContainer();
            ObjectSet<String> selectedNodes = graphContainer.getSelectedNodes();

            Array<NodesInClipboard.NodesData> nodesData = new Array<>();
            Array<GraphConnection> graphConnections = new Array<>();

            ObjectSet<String> copiedNodes = new ObjectSet<>();

            for (GraphBox graphBox : graphContainer.getGraphBoxes()) {
                if (selectedNodes.contains(graphBox.getId()) && !graphBox.getId().equals("end")) {
                    VisWindow boxWindow = graphContainer.getBoxWindow(graphBox.getId());
                    nodesData.add(
                            new NodesInClipboard.NodesData(
                                    new GraphNodeImpl(graphBox.getId(), graphBox.getData(), graphBox.getConfiguration()),
                                    boxWindow.getX(), boxWindow.getY()));
                    copiedNodes.add(graphBox.getId());
                }
            }

            for (GraphConnection connection : graphContainer.getConnections()) {
                if (copiedNodes.contains(connection.getNodeFrom()) && copiedNodes.contains(connection.getNodeTo()))
                    graphConnections.add(connection);
            }

            nodesInClipboard.graphType = type;
            nodesInClipboard.nodesData = nodesData;
            nodesInClipboard.graphConnections = graphConnections;
        }
    }

    private void paste() {
        GraphDesignTab activeTab = (GraphDesignTab) tabbedPane.getActiveTab();
        if (activeTab != null) {
            GraphType type = activeTab.getType();
            if (nodesInClipboard.graphType != null && type.getType().equals(nodesInClipboard.graphType.getType())) {
                ObjectMap<String, String> oldToNewIdMapping = new ObjectMap<>();
                UIGraphConfiguration[] uiGraphConfigurations = activeTab.getUiGraphConfigurations();

                // Do the actual paste
                for (NodesInClipboard.NodesData nodesDatum : nodesInClipboard.nodesData) {
                    String id = UUID.randomUUID().toString().replace("-", "");
                    GraphBoxProducer graphBoxProducer = findGraphBoxProducer(uiGraphConfigurations, nodesDatum.graphNode.getConfiguration().getType());
                    GraphBox graphBox = graphBoxProducer.createPipelineGraphBox(skin, id, nodesDatum.graphNode.getData());
                    activeTab.getGraphContainer().addGraphBox(graphBox, nodesDatum.graphNode.getConfiguration().getName(),
                            true, nodesDatum.x, nodesDatum.y);
                    oldToNewIdMapping.put(nodesDatum.graphNode.getId(), id);
                }

                for (GraphConnection graphConnection : nodesInClipboard.graphConnections) {
                    activeTab.getGraphContainer().addGraphConnection(
                            oldToNewIdMapping.get(graphConnection.getNodeFrom()), graphConnection.getFieldFrom(),
                            oldToNewIdMapping.get(graphConnection.getNodeTo()), graphConnection.getFieldTo());
                }
            }
        }
    }

    private GraphBoxProducer findGraphBoxProducer(UIGraphConfiguration[] configurations, String type) {
        for (UIGraphConfiguration configuration : configurations) {
            for (GraphBoxProducer graphBoxProducer : configuration.getGraphBoxProducers()) {
                if (graphBoxProducer.getType().equals(type))
                    return graphBoxProducer;
            }
        }

        return null;
    }

    private void closeApplication() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            Gdx.app.exit();
                        }

                        @Override
                        public void no() {
                            Gdx.app.exit();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            Gdx.app.exit();
        }
    }

    private void closePipeline() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            removeAllTabs();
                            savedGraphs.clear();
                            graphDesignTab = null;
                        }

                        @Override
                        public void no() {
                            removeAllTabs();
                            savedGraphs.clear();
                            graphDesignTab = null;
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            removeAllTabs();
            savedGraphs.clear();
            graphDesignTab = null;
        }
    }

    private void open() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();

            FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
            fileChooser.setModal(true);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {
                    FileHandle selectedFile = file.get(0);
                    loadPipelineFromFile(selectedFile);
                    editedFile = selectedFile;
                    graphDesignTab.setDirty(false);
                }
            });
            getStage().addActor(fileChooser.fadeIn());
        }
    }

    private void createGroup() {
        ((GraphDesignTab) tabbedPane.getActiveTab()).getGraphContainer().createNodeGroup();
    }

    private void exportShader() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                    selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                }
                writeGraph(selectedFile, ((GraphDesignTab) tabbedPane.getActiveTab()).serializeGraph());
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void copyShader() {
        GraphDesignTab activeTab = (GraphDesignTab) tabbedPane.getActiveTab();
        graphInClipboard.graphType = activeTab.getType();
        graphInClipboard.graph = activeTab.serializeGraph();
    }

    private void saveAs() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                    selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                }
                editedFile = selectedFile;
                saveToFile(graphDesignTab, selectedFile);
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void save() {
        if (graphDesignTab != null) {
            if (editedFile == null) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
                fileChooser.setModal(true);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        FileHandle selectedFile = file.get(0);
                        if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                            selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                        }
                        editedFile = selectedFile;
                        saveToFile(graphDesignTab, selectedFile);
                    }
                });
                getStage().addActor(fileChooser.fadeIn());
            } else {
                saveToFile(graphDesignTab, editedFile);
            }
        }
    }

    private void saveToFile(GraphDesignTab graphDesignTab, FileHandle editedFile) {
        for (Tab tab : tabbedPane.getTabs()) {
            tab.save();
        }
        JsonValue graph = graphDesignTab.serializeGraph();

        writeGraph(editedFile, graph);

        graphDesignTab.setDirty(false);
    }

    private void writeGraph(FileHandle editedFile, JsonValue graph) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(editedFile.write(false));
            try {
                graph.prettyPrint(JsonWriter.OutputType.json, out);
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private PopupMenu createTemplateMenu() {
        PopupMenu templateMenu = new PopupMenu();
        MenuItem menuItem = new MenuItem("Empty");
        menuItem.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        loadPipelineFromFile(Gdx.files.classpath("template/empty-pipeline.json"));
                    }
                });
        templateMenu.addItem(menuItem);
        return templateMenu;
    }

    private void loadPipelineFromFile(FileHandle fileHandle) {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();
            try {
                InputStream stream = fileHandle.read();
                try {
                    UIPipelineConfiguration pipelineGraphConfiguration = new UIPipelineConfiguration();
                    graphDesignTab = GraphLoader.loadGraph(stream, new UIGraphLoaderCallback(
                            skin, new GraphDesignTab(false, RenderPipelineGraphType.instance, "main", "Render pipeline", skin,
                            null, pipelineGraphConfiguration), new PropertyLocation[0], pipelineGraphConfiguration));

                    graphDesignTab.getContentTable().addListener(
                            new EventListener() {
                                @Override
                                public boolean handle(Event event) {
                                    if (event instanceof RequestGraphOpen) {
                                        RequestGraphOpen request = (RequestGraphOpen) event;
                                        Tab tab = findTabByGraphId(request.getId());
                                        if (tab != null) {
                                            tabbedPane.switchTab(tab);
                                        } else {
                                            JsonValue graph = savedGraphs.get(request.getId());
                                            if (graph == null)
                                                graph = request.getJsonObject();
                                            openGraphTab(request.getId(), graph, request.getTitle(), request.getType(),
                                                    request.getGraphConfigurations());
                                        }
                                        return true;
                                    }
                                    if (event instanceof GetSerializedGraph) {
                                        GetSerializedGraph request = (GetSerializedGraph) event;
                                        request.setGraph(savedGraphs.get(request.getId()));
                                        return true;
                                    }
                                    if (event instanceof GraphRemoved) {
                                        GraphRemoved removed = (GraphRemoved) event;
                                        Tab tab = findTabByGraphId(removed.getId());
                                        if (tab != null) {
                                            tabbedPane.remove(tab);
                                        }
                                    }
                                    return false;
                                }
                            });

                    tabbedPane.add(graphDesignTab);
                    editedFile = null;
                } finally {
                    stream.close();
                }
            } catch (IOException exp) {
                throw new RuntimeException("Unable to load default pipeline definition", exp);
            }
        }
    }

    private void removeAllTabs() {
        tabbedPane.removeAll();
        insideTable.clearChildren();
        insideTable.add(new EmptyTabWidget());
    }

    public void dispose() {
        for (Tab tab : tabbedPane.getTabs()) {
            tab.dispose();
        }
    }

    private static class GraphNodeImpl implements GraphNode {
        private String id;
        private JsonValue data;
        private NodeConfiguration configuration;

        public GraphNodeImpl(String id, JsonValue data, NodeConfiguration configuration) {
            this.id = id;
            this.data = data;
            this.configuration = configuration;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public JsonValue getData() {
            return data;
        }

        @Override
        public NodeConfiguration getConfiguration() {
            return configuration;
        }
    }
}

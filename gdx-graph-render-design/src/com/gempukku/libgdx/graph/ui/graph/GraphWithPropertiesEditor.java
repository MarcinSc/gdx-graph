package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.GraphChangedListener;
import com.gempukku.libgdx.ui.graph.GraphEditor;
import com.gempukku.libgdx.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.ui.graph.data.NodeGroup;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;
import com.gempukku.libgdx.ui.graph.validator.GraphValidator;
import com.gempukku.libgdx.ui.preview.PreviewWidget;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.UndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.gempukku.libgdx.undo.event.UndoableChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GraphWithPropertiesEditor extends VisTable {
    private final UIGraphType type;
    private final Skin skin;

    private final List<PropertyEditor> propertyEditors = new LinkedList<>();
    private final CompositeGraphWithProperties compositeGraphWithProperties;
    private final GraphEditor graphEditor;

    private final VerticalGroup pipelineProperties;
    private final VisLabel validationLabel;

    public GraphWithPropertiesEditor(GraphWithProperties graph) {
        this.type = (UIGraphType) GraphTypeRegistry.findGraphType(graph.getType());
        this.skin = VisUI.getSkin();

        pipelineProperties = createPropertiesUI();
        pipelineProperties.addListener(
                new UndoableChangeListener() {
                    @Override
                    public void changed(UndoableChangeEvent event) {
                        GraphChangedEvent graphChangedEvent = Pools.obtain(GraphChangedEvent.class);
                        graphChangedEvent.setData(true);
                        graphChangedEvent.setUndoableAction(event.getUndoableAction());
                        pipelineProperties.fire(graphChangedEvent);
                        Pools.free(graphChangedEvent);
                        event.stop();
                    }
                });

        graphEditor = new GraphEditor(graph,
                new Function<String, GraphNodeEditorProducer>() {
                    @Override
                    public GraphNodeEditorProducer evaluate(String value) {
                        if (value.equals("Property")) {
                            return new PropertyGraphNodeEditorProducer(type.getUIConfigurations());
                        }

                        for (UIGraphConfiguration graphConfiguration : type.getUIConfigurations()) {
                            for (MenuGraphNodeEditorProducer graphNodeEditorProducer : graphConfiguration.getGraphNodeEditorProducers()) {
                                if (graphNodeEditorProducer.getType().equals(value))
                                    return graphNodeEditorProducer;
                            }
                        }

                        return null;
                    }
                },
                new PopupMenuProducer() {
                    @Override
                    public PopupMenu createPopupMenu(float x, float y) {
                        return createGraphPopupMenu(x, y);
                    }
                }, "gdx-graph");
        this.compositeGraphWithProperties = new CompositeGraphWithProperties(graphEditor.getGraph(), propertyEditors);

        this.addListener(
                new GraphChangedListener() {
                    @Override
                    protected boolean graphChanged(GraphChangedEvent event) {
                        processGraphChanged(event);
                        return false;
                    }
                });


        VisTable leftTable = new VisTable();
        VisTable headerTable = new VisTable();
        headerTable.pad(2);
        headerTable.add(new VisLabel("Properties", "gdx-graph-property-label")).growX();
        final VisTextButton newPropertyButton = new VisTextButton("Add", "gdx-graph-property-label");
        newPropertyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PopupMenu popupMenu = createPropertyPopupMenu();
                        popupMenu.showMenu(pipelineProperties.getStage(), newPropertyButton);
                    }
                });
        headerTable.add(newPropertyButton);
        headerTable.row();
        leftTable.add(headerTable).growX().row();

        VisScrollPane propertiesScroll = new VisScrollPane(pipelineProperties);
        propertiesScroll.setFadeScrollBars(false);
        propertiesScroll.setScrollingDisabled(true, false);

        leftTable.add(propertiesScroll).grow().row();
        PreviewWidget previewWidget = new PreviewWidget(graphEditor, "gdx-graph");
        leftTable.add(previewWidget).growX().height(200).row();
        HorizontalGroup buttons = new HorizontalGroup();
        VisTextButton center = new VisTextButton("Center");
        center.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        graphEditor.centerCanvas();
                        event.stop();
                    }
                });
        buttons.addActor(center);
        validationLabel = new VisLabel("Invalid");
        validationLabel.setColor(Color.RED);
        buttons.addActor(validationLabel);
        buttons.align(Align.left);
        leftTable.add(buttons).growX().row();

        VisSplitPane splitPane = new VisSplitPane(leftTable, graphEditor, false);

        splitPane.setMaxSplitAmount(0.2f);
        splitPane.setSplitAmount(0.2f);

        this.add(splitPane).grow().row();

        for (GraphProperty property : graph.getProperties()) {
            PropertyEditorDefinition propertyEditorDefinition = getPropertyEditorDefinition(property.getType());
            PropertyEditor propertyEditor = propertyEditorDefinition.createPropertyEditor(property.getName(), property.getData());
            addPropertyEditor(property.getName(), propertyEditor);
        }

        GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
        event.setStructure(true);
        event.setData(true);
        processGraphChanged(event);
        Pools.free(event);
    }

    private void graphChanged(boolean structure, boolean data, UndoableAction undoableAction) {
        GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
        event.setStructure(true);
        event.setData(true);
        event.setUndoableAction(undoableAction);
        fire(event);
        Pools.free(event);
    }

    private PropertyEditorDefinition getPropertyEditorDefinition(String propertyType) {
        for (UIGraphConfiguration graphConfiguration : type.getUIConfigurations()) {
            PropertyEditorDefinition propertyEditorDefinition = graphConfiguration.getPropertyEditorDefinitions().get(propertyType);
            if (propertyEditorDefinition != null) {
                return propertyEditorDefinition;
            }
        }
        return null;
    }

    public GraphWithProperties getGraph() {
        return compositeGraphWithProperties;
    }

    private PopupMenu createGraphPopupMenu(final float popupX, final float popupY) {
        PopupMenu popupMenu = new PopupMenu();

        for (UIGraphConfiguration uiGraphConfiguration : type.getUIConfigurations()) {
            boolean hasChild = false;
            for (final MenuGraphNodeEditorProducer producer : uiGraphConfiguration.getGraphNodeEditorProducers()) {
                String menuLocation = producer.getMenuLocation();
                if (menuLocation != null) {
                    String[] menuSplit = menuLocation.split("/");
                    PopupMenu targetMenu = findOrCreatePopupMenu(popupMenu, menuSplit, 0);
                    final String title = producer.getName();
                    MenuItem valueMenuItem = new MenuItem(title);
                    valueMenuItem.addListener(
                            new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    String id = UUID.randomUUID().toString().replace("-", "");
                                    graphEditor.addGraphNode(id, producer.getType(), null, popupX, popupY);
                                }
                            });
                    targetMenu.addItem(valueMenuItem);
                    hasChild = true;
                }
            }
            if (hasChild)
                popupMenu.addSeparator();
        }
        MenuItem propertyMenuItem = new MenuItem("Property");
        propertyMenuItem.setDisabled(true);
        popupMenu.addItem(propertyMenuItem);

        if (!propertyEditors.isEmpty()) {
            PopupMenu propertyMenu = new PopupMenu();
            for (final PropertyEditor propertyProducer : propertyEditors) {
                final String name = propertyProducer.getName();
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                String id = UUID.randomUUID().toString().replace("-", "");
                                String name = propertyProducer.getName();
                                String type = propertyProducer.getType();
                                JsonValue data = new JsonValue(JsonValue.ValueType.object);
                                data.addChild("name", new JsonValue(name));
                                data.addChild("type", new JsonValue(type));
                                graphEditor.addGraphNode(id, "Property", data, popupX, popupY);
                            }
                        });
                propertyMenu.addItem(valueMenuItem);
            }
            propertyMenuItem.setSubMenu(propertyMenu);
            propertyMenuItem.setDisabled(false);
        }

        return popupMenu;
    }

    public boolean canGroupNodes() {
        ObjectSet<String> selectedNodes = graphEditor.getSelectedNodes();
        if (selectedNodes.size == 0)
            return false;
        for (NodeGroup group : graphEditor.getGraph().getGroups()) {
            for (String selectedNode : selectedNodes) {
                if (group.getNodeIds().contains(selectedNode))
                    return false;
            }
        }
        return true;
    }

    public void createGroup(String groupName) {
        if (canGroupNodes()) {
            ObjectSet<String> selectedNodes = graphEditor.getSelectedNodes();
            graphEditor.addNodeGroup(groupName, selectedNodes);
        }
    }

    private PopupMenu findOrCreatePopupMenu(PopupMenu popupMenu, String[] menuSplit, int startIndex) {
        for (Actor child : popupMenu.getChildren()) {
            if (child instanceof MenuItem) {
                MenuItem childMenuItem = (MenuItem) child;
                if (childMenuItem.getLabel().getText().toString().equals(menuSplit[startIndex]) && childMenuItem.getSubMenu() != null) {
                    if (startIndex + 1 < menuSplit.length) {
                        return findOrCreatePopupMenu(childMenuItem.getSubMenu(), menuSplit, startIndex + 1);
                    } else {
                        return childMenuItem.getSubMenu();
                    }
                }
            }
        }

        PopupMenu createdPopup = new PopupMenu();
        MenuItem createdMenuItem = new MenuItem(menuSplit[startIndex]);
        createdMenuItem.setSubMenu(createdPopup);
        popupMenu.addItem(createdMenuItem);
        if (startIndex + 1 < menuSplit.length) {
            return findOrCreatePopupMenu(createdPopup, menuSplit, startIndex + 1);
        } else {
            return createdPopup;
        }
    }

    private PopupMenu createPropertyPopupMenu() {
        PopupMenu menu = new PopupMenu();
        for (UIGraphConfiguration uiGraphConfiguration : type.getUIConfigurations()) {
            for (Map.Entry<String, ? extends PropertyEditorDefinition> propertyEntry : uiGraphConfiguration.getPropertyEditorDefinitions().entrySet()) {
                final String name = propertyEntry.getKey();
                final PropertyEditorDefinition value = propertyEntry.getValue();
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                String defaultName = value.getDefaultName();
                                String propertyName = resolveUniqueName(defaultName);
                                PropertyEditor defaultPropertyEditor = value.createPropertyEditor(propertyName, null);
                                addPropertyEditor(name, defaultPropertyEditor);
                            }
                        });
                menu.addItem(valueMenuItem);
            }
        }

        return menu;
    }

    private String resolveUniqueName(String defaultName) {
        if (hasPropertyName(defaultName)) {
            int index = 0;
            while (true) {
                String prospectiveName = defaultName + " " + index;
                if (!hasPropertyName(prospectiveName))
                    return prospectiveName;
                index++;
            }
        }
        return defaultName;
    }

    private boolean hasPropertyName(String name) {
        for (PropertyEditor propertyEditor : propertyEditors) {
            if (propertyEditor.getName().equals(name))
                return true;
        }
        return false;
    }

    private void processGraphChanged(GraphChangedEvent event) {
        GraphWithProperties graph = getGraph();

        GraphValidator graphValidator = type.getGraphValidator();
        GraphValidationResult validationResult = graphValidator.validateGraph(graph, type.getStartNodeIdForValidation());
        graphEditor.setValidationResult(validationResult);
        if (validationResult.hasErrors()) {
            validationLabel.setColor(Color.RED);
            validationLabel.setText("Invalid");
        } else if (validationResult.hasWarnings()) {
            validationLabel.setColor(Color.YELLOW);
            validationLabel.setText("Acceptable");
        } else {
            validationLabel.setColor(Color.GREEN);
            validationLabel.setText("OK");
        }

        for (UIGraphConfiguration uiGraphConfiguration : type.getUIConfigurations()) {
            for (MenuGraphNodeEditorProducer graphNodeEditorProducer : uiGraphConfiguration.getGraphNodeEditorProducers()) {
                if (graphNodeEditorProducer instanceof GraphChangedAware) {
                    ((GraphChangedAware) graphNodeEditorProducer).graphChanged(event, graph);
                }
            }
        }
    }

    private VerticalGroup createPropertiesUI() {
        final VerticalGroup pipelineProperties = new VerticalGroup();
        pipelineProperties.grow();
        return pipelineProperties;
    }

    public void addPropertyEditor(String type, final PropertyEditor propertyEditor) {
        final Actor actor = propertyEditor.getActor();

        final VisTable table = new VisTable();
        table.pad(2);
        final Drawable window = skin.getDrawable("graph-window-selected");
        BaseDrawable wrapper = new BaseDrawable(window) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                window.draw(batch, x, y, width, height);
            }
        };
        wrapper.setTopHeight(3);
        table.setBackground(wrapper);
        table.add(new VisLabel(type, "gdx-graph-property-label")).growX();
        VisImageButton removeButton = new VisImageButton("gdx-graph-window-close");
        removeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        removePropertyEditor(propertyEditor);
                    }
                });
        table.add(removeButton);
        table.row();
        table.add(actor).colspan(2).growX();
        table.row();

        int index = pipelineProperties.getChildren().size;
        AddPropertyEditorAction addPropertyAction = new AddPropertyEditorAction(propertyEditor, table, index);
        addPropertyAction.doAction();

        graphChanged(true, false, addPropertyAction);
    }

    private void removePropertyEditor(PropertyEditor propertyEditor) {
        Actor actor = propertyEditor.getActor();
        VisTable parent = (VisTable) actor.getParent();

        int index = propertyEditors.indexOf(propertyEditor);
        RemovePropertyEditorAction removePropertyAction = new RemovePropertyEditorAction(propertyEditor, parent, index);
        removePropertyAction.doAction();

        graphChanged(true, false, removePropertyAction);
    }

    private class AddPropertyEditorAction extends DefaultUndoableAction {
        private PropertyEditor propertyEditor;
        private VisTable ui;
        private int index;

        public AddPropertyEditorAction(PropertyEditor propertyEditor, VisTable ui, int index) {
            this.propertyEditor = propertyEditor;
            this.ui = ui;
            this.index = index;
        }

        @Override
        public void undoAction() {
            pipelineProperties.removeActor(ui);
            propertyEditors.remove(propertyEditor);
            graphChanged(true, false, null);
        }

        @Override
        public void redoAction() {
            propertyEditors.add(propertyEditor);
            pipelineProperties.addActorAt(index, ui);
            graphChanged(true, false, null);
        }
    }

    private class RemovePropertyEditorAction extends DefaultUndoableAction {
        private PropertyEditor propertyEditor;
        private VisTable ui;
        private int index;

        public RemovePropertyEditorAction(PropertyEditor propertyEditor, VisTable ui, int index) {
            this.propertyEditor = propertyEditor;
            this.ui = ui;
            this.index = index;
        }

        @Override
        public void undoAction() {
            propertyEditors.add(propertyEditor);
            pipelineProperties.addActorAt(index, ui);
            graphChanged(true, false, null);
        }

        @Override
        public void redoAction() {
            pipelineProperties.removeActor(ui);
            propertyEditors.remove(propertyEditor);
            graphChanged(true, false, null);
        }
    }
}

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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.DirtyHierarchy;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
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
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GraphWithPropertiesEditor extends VisTable implements Disposable {
    private final UIGraphType type;
    private final Skin skin;

    private final List<PropertyBox> propertyBoxes = new LinkedList<>();
    private final CompositeGraphWithProperties compositeGraphWithProperties;
    private final GraphEditor graphEditor;

    private final VerticalGroup pipelineProperties;
    private final VisLabel validationLabel;

    public GraphWithPropertiesEditor(GraphWithProperties graph, DirtyHierarchy dirtyHierarchy) {
        this.type = (UIGraphType) GraphTypeRegistry.findGraphType(graph.getType());
        this.skin = VisUI.getSkin();

        pipelineProperties = createPropertiesUI();

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
        this.compositeGraphWithProperties = new CompositeGraphWithProperties(graphEditor.getGraph(), propertyBoxes);

        this.addListener(
                new GraphChangedListener() {
                    @Override
                    protected boolean graphChanged(GraphChangedEvent event) {
                        dirtyHierarchy.setDirty();
                        processGraphChanged(event);

                        event.stop();
                        return true;
                    }
                });


        VisTable leftTable = new VisTable();

        VisScrollPane propertiesScroll = new VisScrollPane(pipelineProperties);
        propertiesScroll.setFadeScrollBars(false);

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
            PropertyBox propertyBox = propertyEditorDefinition.createPropertyBox(property.getName(), property.getLocation(), property.getData(), type.getPropertyLocations());
            addPropertyBox(property.getName(), propertyBox);
        }

        processGraphChanged(new GraphChangedEvent(false, false));
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

        if (!propertyBoxes.isEmpty()) {
            PopupMenu propertyMenu = new PopupMenu();
            for (final PropertyBox propertyProducer : propertyBoxes) {
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
                                PropertyBox defaultPropertyBox = value.createPropertyBox(propertyName, null, null, type.getPropertyLocations());
                                addPropertyBox(name, defaultPropertyBox);
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
        for (PropertyBox propertyBox : propertyBoxes) {
            if (propertyBox.getName().equals(name))
                return true;
        }
        return false;
    }

    private void processGraphChanged(GraphChangedEvent event) {
        GraphWithProperties graph = getGraph();

        GraphValidator graphValidator = type.getGraphValidator();
        GraphValidationResult validationResult = graphValidator.validateGraph(graph);
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
                    ((GraphChangedAware) graphNodeEditorProducer).graphChanged(event, validationResult.hasErrors(), graph);
                }
            }
        }
    }

    private VerticalGroup createPropertiesUI() {
        final VerticalGroup pipelineProperties = new VerticalGroup();
        pipelineProperties.grow();
        VisTable headerTable = new VisTable();
        headerTable.setBackground(skin.getDrawable("vis-blue"));
        headerTable.add(new VisLabel("Properties")).growX();
        final VisTextButton newPropertyButton = new VisTextButton("Add", "menu-bar");
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
        pipelineProperties.addActor(headerTable);
        return pipelineProperties;
    }

    public void addPropertyBox(String type, final PropertyBox propertyBox) {
        propertyBoxes.add(propertyBox);
        final Actor actor = propertyBox.getActor();

        final VisTable table = new VisTable();
        final Drawable window = skin.getDrawable("window");
        BaseDrawable wrapper = new BaseDrawable(window) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                window.draw(batch, x, y, width, height);
            }
        };
        wrapper.setTopHeight(3);
        table.setBackground(wrapper);
        table.add(new VisLabel(type)).growX();
        VisImageButton removeButton = new VisImageButton("close-window");
        removeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        removePropertyBox(propertyBox);
                    }
                });
        table.add(removeButton);
        table.row();
        table.add(actor).colspan(2).growX();
        table.row();
        pipelineProperties.addActor(table);

        this.fire(new GraphChangedEvent(true, false));
    }

    private void removePropertyBox(PropertyBox propertyBox) {
        Actor actor = propertyBox.getActor();
        propertyBoxes.remove(propertyBox);
        pipelineProperties.removeActor(actor.getParent());
        propertyBox.dispose();

        this.fire(new GraphChangedEvent(true, false));
    }

    @Override
    public void dispose() {
        graphEditor.dispose();
        for (PropertyBox propertyBox : propertyBoxes) {
            propertyBox.dispose();
        }
    }
}

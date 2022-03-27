package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeGroup;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.preview.PreviewWidget;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

import java.util.*;

public class GraphDesignTab extends Tab implements Graph<GraphBox, GraphConnection, PropertyBox> {
    private List<PropertyBox> propertyBoxes = new LinkedList<>();
    private final GraphContainer graphContainer;

    private Skin skin;
    private UIGraphConfiguration[] uiGraphConfigurations;
    private SaveCallback saveCallback;

    private GraphType type;
    private String id;
    private String title;

    private final VerticalGroup pipelineProperties;
    private VisTable contentTable;
    private VisLabel validationLabel;

    private GraphValidator<GraphBox, GraphConnection, PropertyBox> graphValidator = new GraphValidator<>();

    private boolean finishedLoading = false;

    public GraphDesignTab(boolean closeable, GraphType type, String id, String title, Skin skin,
                          SaveCallback saveCallback, UIGraphConfiguration... uiGraphConfiguration) {
        super(true, closeable);
        this.type = type;
        this.id = id;
        this.title = title;

        contentTable = new VisTable();
        pipelineProperties = createPropertiesUI(skin);
        this.skin = skin;
        this.uiGraphConfigurations = uiGraphConfiguration;
        this.saveCallback = saveCallback;

        graphContainer = new GraphContainer(skin,
                new PopupMenuProducer() {
                    @Override
                    public PopupMenu createPopupMenu(float x, float y) {
                        return createGraphPopupMenu(x, y);
                    }
                });
        contentTable.addListener(
                new GraphChangedListener() {
                    @Override
                    protected boolean graphChanged(GraphChangedEvent event) {
                        if (finishedLoading) {
                            setDirty(true);
                            if (event.isStructure())
                                updatePipelineValidation();
                            for (GraphBox graphBox : graphContainer.getGraphBoxes()) {
                                graphBox.graphChanged(event, graphContainer.getValidationResult().hasErrors(),
                                        GraphDesignTab.this);
                            }
                        }

                        event.stop();
                        return true;
                    }
                });


        VisTable leftTable = new VisTable();

        VisScrollPane propertiesScroll = new VisScrollPane(pipelineProperties);
        propertiesScroll.setFadeScrollBars(false);

        leftTable.add(propertiesScroll).grow().row();
        PreviewWidget previewWidget = new PreviewWidget(graphContainer);
        leftTable.add(previewWidget).growX().height(200).row();
        HorizontalGroup buttons = new HorizontalGroup();
        VisTextButton center = new VisTextButton("Center");
        center.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        graphContainer.centerCanvas();
                        event.stop();
                    }
                });
        buttons.addActor(center);
        validationLabel = new VisLabel("Invalid");
        validationLabel.setColor(Color.RED);
        buttons.addActor(validationLabel);
        buttons.align(Align.left);
        leftTable.add(buttons).growX().row();

        VisSplitPane splitPane = new VisSplitPane(leftTable, graphContainer, false);

        splitPane.setMaxSplitAmount(0.2f);
        splitPane.setSplitAmount(0.2f);

        contentTable.add(splitPane).grow().row();
    }

    public UIGraphConfiguration[] getUiGraphConfigurations() {
        return uiGraphConfigurations;
    }

    public GraphType getType() {
        return type;
    }

    @Override
    public PropertyBox getPropertyByName(String name) {
        for (PropertyBox propertyBox : propertyBoxes) {
            if (propertyBox.getName().equals(name))
                return propertyBox;
        }
        return null;
    }

    @Override
    public Iterable<? extends GraphConnection> getConnections() {
        return graphContainer.getConnections();
    }

    @Override
    public Iterable<? extends PropertyBox> getProperties() {
        return propertyBoxes;
    }

    public void finishedLoading() {
        finishedLoading = true;
        contentTable.fire(new GraphChangedEvent(true, false));
        graphContainer.adjustCanvas();
    }

    @Override
    public boolean save() {
        super.save();

        if (saveCallback != null)
            saveCallback.save(this);
        setDirty(false);

        return true;
    }

    private PopupMenu createGraphPopupMenu(final float popupX, final float popupY) {
        PopupMenu popupMenu = new PopupMenu();

        for (UIGraphConfiguration uiGraphConfiguration : uiGraphConfigurations) {
            boolean hasChild = false;
            for (final GraphBoxProducer producer : uiGraphConfiguration.getGraphBoxProducers()) {
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
                                    GraphBox graphBox = producer.createDefault(skin, id);
                                    graphContainer.addGraphBox(graphBox, title, true, popupX, popupY);
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
                                GraphBox graphBox = propertyProducer.createPropertyBox(skin, id, popupX, popupY);
                                graphContainer.addGraphBox(graphBox, "Property", true, popupX, popupY);
                            }
                        });
                propertyMenu.addItem(valueMenuItem);
            }
            propertyMenuItem.setSubMenu(propertyMenu);
            propertyMenuItem.setDisabled(false);
        }

        return popupMenu;
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

    private PopupMenu createPropertyPopupMenu(float x, float y) {
        PopupMenu menu = new PopupMenu();
        for (UIGraphConfiguration uiGraphConfiguration : uiGraphConfigurations) {
            for (Map.Entry<String, PropertyBoxProducer> propertyEntry : uiGraphConfiguration.getPropertyBoxProducers().entrySet()) {
                final String name = propertyEntry.getKey();
                final PropertyBoxProducer value = propertyEntry.getValue();
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                PropertyBox defaultPropertyBox = value.createDefaultPropertyBox(skin, type.getPropertyLocations());
                                addPropertyBox(name, defaultPropertyBox);
                            }
                        });
                menu.addItem(valueMenuItem);
            }
        }

        return menu;
    }

    public String getId() {
        return id;
    }

    @Override
    public GraphBox getNodeById(String id) {
        return graphContainer.getGraphBoxById(id);
    }

    @Override
    public Iterable<? extends GraphBox> getNodes() {
        return graphContainer.getGraphBoxes();
    }

    public GraphContainer getGraphContainer() {
        return graphContainer;
    }

    private void updatePipelineValidation() {
        GraphValidator.ValidationResult<GraphBox, GraphConnection, PropertyBox> validationResult = graphValidator.validateGraph(this, "end");
        graphContainer.setValidationResult(validationResult);
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
    }

    private VerticalGroup createPropertiesUI(final Skin skin) {
        final VerticalGroup pipelineProperties = new VerticalGroup();
        pipelineProperties.grow();
        VisTable headerTable = new VisTable();
        headerTable.setBackground(skin.getDrawable("vis-blue"));
        headerTable.add(new VisLabel("Properties")).growX();
        final VisTextButton newPropertyButton = new VisTextButton("Add", "menu-bar");
        newPropertyButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        PopupMenu popupMenu = createPropertyPopupMenu(x, y);
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

        contentTable.fire(new GraphChangedEvent(true, false));
    }

    private void removePropertyBox(PropertyBox propertyBox) {
        Actor actor = propertyBox.getActor();
        propertyBoxes.remove(propertyBox);
        pipelineProperties.removeActor(actor.getParent());
        propertyBox.dispose();

        contentTable.fire(new GraphChangedEvent(true, false));
    }

    @Override
    public void dispose() {
        graphContainer.dispose();
        for (PropertyBox propertyBox : propertyBoxes) {
            propertyBox.dispose();
        }
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public VisTable getContentTable() {
        return contentTable;
    }

    public JsonValue serializeGraph() {
        JsonValue graph = new JsonValue(JsonValue.ValueType.object);
        graph.addChild("version", new JsonValue(GraphLoader.VERSION));
        graph.addChild("type", new JsonValue(type.getType()));

        JsonValue objects = new JsonValue(JsonValue.ValueType.array);
        for (JsonValue jsonValue : getSortedNodesAsJson()) {
            objects.addChild(jsonValue);
        }
        graph.addChild("nodes", objects);

        List<JsonValue> connectionJsonValues = getSortedConnectionsAsJson();
        JsonValue connections = new JsonValue(JsonValue.ValueType.array);
        for (JsonValue connection : connectionJsonValues) {
            connections.addChild(connection);
        }
        graph.addChild("connections", connections);

        JsonValue properties = new JsonValue(JsonValue.ValueType.array);
        for (PropertyBox propertyBox : propertyBoxes) {
            JsonValue property = new JsonValue(JsonValue.ValueType.object);
            property.addChild("name", new JsonValue(propertyBox.getName()));
            property.addChild("type", new JsonValue(propertyBox.getType()));
            PropertyLocation location = propertyBox.getLocation();
            if (location != null)
                property.addChild("location", new JsonValue(location.name()));

            JsonValue data = propertyBox.getData();
            if (data != null)
                property.addChild("data", data);

            properties.addChild(property);
        }
        graph.addChild("properties", properties);

        JsonValue groups = new JsonValue(JsonValue.ValueType.array);
        for (NodeGroup nodeGroup : graphContainer.getNodeGroups()) {
            JsonValue group = new JsonValue(JsonValue.ValueType.object);
            group.addChild("name", new JsonValue(nodeGroup.getName()));
            JsonValue nodes = new JsonValue(JsonValue.ValueType.array);
            for (String nodeId : nodeGroup.getNodeIds()) {
                nodes.addChild(new JsonValue(nodeId));
            }
            group.addChild("nodes", nodes);
            groups.addChild(group);
        }
        graph.addChild("groups", groups);

        return graph;
    }

    private List<JsonValue> getSortedConnectionsAsJson() {
        List<JsonValue> connectionJsonValues = new ArrayList<>();
        for (GraphConnection connection : graphContainer.getConnections()) {
            JsonValue conn = new JsonValue(JsonValue.ValueType.object);
            conn.addChild("fromNode", new JsonValue(connection.getNodeFrom()));
            conn.addChild("fromField", new JsonValue(connection.getFieldFrom()));
            conn.addChild("toNode", new JsonValue(connection.getNodeTo()));
            conn.addChild("toField", new JsonValue(connection.getFieldTo()));
            connectionJsonValues.add(conn);
        }
        // Sort the connections
        Collections.sort(connectionJsonValues, new Comparator<JsonValue>() {
            @Override
            public int compare(JsonValue o1, JsonValue o2) {
                String s1 = getNodeString(o1);
                String s2 = getNodeString(o2);
                return s1.compareTo(s2);
            }

            private String getNodeString(JsonValue node) {
                return node.getString("fromNode") + "." + node.getString("fromField") + "." + node.getString("toNode") + "." + node.getString("toField");
            }
        });
        return connectionJsonValues;
    }

    private List<JsonValue> getSortedNodesAsJson() {
        List<JsonValue> nodeList = new ArrayList<>();
        Vector2 tmp = new Vector2();
        graphContainer.getCanvasPosition(tmp);
        for (GraphBox graphBox : graphContainer.getGraphBoxes()) {
            VisWindow window = graphContainer.getBoxWindow(graphBox.getId());
            JsonValue object = new JsonValue(JsonValue.ValueType.object);
            object.addChild("id", new JsonValue(graphBox.getId()));
            object.addChild("type", new JsonValue(graphBox.getConfiguration().getType()));
            object.addChild("x", new JsonValue(tmp.x + window.getX()));
            object.addChild("y", new JsonValue(tmp.y + window.getY()));

            JsonValue data = graphBox.getData();
            if (data != null)
                object.addChild("data", data);

            nodeList.add(object);
        }
        // Sort the nodes
        Collections.sort(nodeList, new Comparator<JsonValue>() {
            @Override
            public int compare(JsonValue o1, JsonValue o2) {
                return o1.getString("id").compareTo(o2.getString("id"));
            }
        });
        return nodeList;
    }
}

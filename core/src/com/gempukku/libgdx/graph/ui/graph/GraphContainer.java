package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.*;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.preview.NavigableCanvas;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;

public class GraphContainer extends VisTable implements NavigableCanvas {
    private static final float CANVAS_GAP = 50f;
    private static final float GROUP_GAP = 10f;
    private static final float GROUP_LABEL_HEIGHT = 20f;
    private static final float CONNECTOR_LENGTH = 10;
    private static final float CONNECTOR_RADIUS = 5;

    private static final Color GROUP_BACKGROUND_COLOR = new Color(1f, 1f, 1f, 0.3f);
    private static final Color LINE_COLOR = Color.WHITE;
    private static final Color VALID_CONNECTOR_COLOR = Color.WHITE;
    private static final Color INVALID_CONNECTOR_COLOR = Color.RED;

    private static final Color INVALID_LABEL_COLOR = Color.RED;
    private static final Color WARNING_LABEL_COLOR = Color.GOLDENROD;
    private static final Color VALID_LABEL_COLOR = Color.WHITE;
    private static final float NODE_GROUP_PADDING = 4f;

    private float canvasX;
    private float canvasY;
    private float canvasWidth;
    private float canvasHeight;
    private boolean navigating;

    private Map<String, GraphBox> graphBoxes = new HashMap<>();
    private Map<String, VisWindow> boxWindows = new HashMap<>();
    private Map<VisWindow, Vector2> windowPositions = new HashMap<>();
    private List<GraphConnection> graphConnections = new LinkedList<>();

    private Map<NodeConnector, Shape> connectionNodeMap = new HashMap<>();
    private Map<GraphConnection, Shape> connections = new HashMap<>();
    private Map<NodeGroupImpl, Rectangle> nodeGroups = new HashMap<>();

    private ShapeRenderer shapeRenderer;

    private NodeConnector drawingFromConnector;
    private GraphValidator.ValidationResult<GraphBox, GraphConnection, PropertyBox> validationResult = new GraphValidator.ValidationResult<>();

    private ObjectSet<String> selectedNodes = new ObjectSet<>();
    private boolean movingSelected = false;
    private Skin skin;
    private PopupMenuProducer popupMenuProducer;

    public GraphContainer(Skin skin, final PopupMenuProducer popupMenuProducer) {
        this.skin = skin;
        this.popupMenuProducer = popupMenuProducer;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        setClip(true);
        setTouchable(Touchable.enabled);

        addListener(
                new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        processRightClick(x, y);
                    }
                });
        addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        processLeftClick(x, y);
                    }
                });
        DragListener dragListener = new DragListener() {
            private float canvasXStart;
            private float canvasYStart;
            private NodeGroup dragGroup;
            private float movedByX = 0;
            private float movedByY = 0;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == GraphContainer.this) {
                    canvasXStart = canvasX;
                    canvasYStart = canvasY;
                    movedByX = 0;
                    movedByY = 0;
                    dragGroup = null;
                    for (Map.Entry<NodeGroupImpl, Rectangle> nodeGroupEntry : nodeGroups.entrySet()) {
                        Rectangle rectangle = nodeGroupEntry.getValue();
                        if (rectangle.contains(x, y) && y > rectangle.y + rectangle.height - GROUP_LABEL_HEIGHT) {
                            // Hit the label
                            dragGroup = nodeGroupEntry.getKey();
                            break;
                        }
                    }
                }
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (event.getTarget() == GraphContainer.this) {
                    if (dragGroup != null) {
                        movingSelected = true;
                        float moveByX = x - getDragStartX() - movedByX;
                        float moveByY = y - getDragStartY() - movedByY;
                        for (String nodeId : dragGroup.getNodeIds()) {
                            getBoxWindow(nodeId).moveBy(moveByX, moveByY);
                        }
                        movedByX += moveByX;
                        movedByY += moveByY;
                        windowsMoved();
                        updateNodeGroups();
                        movingSelected = false;
                    } else {
                        navigateTo(canvasXStart + getDragStartX() - x, canvasYStart + getDragStartY() - y);
                    }
                }
            }
        };
        dragListener.setTapSquareSize(0f);
        addListener(dragListener);
    }

    public void centerCanvas() {
        navigateTo((canvasWidth - getWidth()) / 2f, (canvasHeight - getHeight()) / 2f);
    }

    @Override
    public void getCanvasPosition(Vector2 result) {
        result.set(canvasX, canvasY);
    }

    @Override
    public void getCanvasSize(Vector2 result) {
        result.set(canvasWidth, canvasHeight);
    }

    @Override
    public void getVisibleSize(Vector2 result) {
        result.set(getWidth(), getHeight());
    }

    @Override
    public void navigateTo(float x, float y) {
        x = MathUtils.round(x);
        y = MathUtils.round(y);

        navigating = true;
        float difX = x - canvasX;
        float difY = y - canvasY;
        for (Actor element : getElements()) {
            element.moveBy(-difX, -difY);
        }
        canvasX = x;
        canvasY = y;
        navigating = false;

        windowsMoved();
    }

    @Override
    public Iterable<? extends Actor> getElements() {
        return boxWindows.values();
    }

    private void updateCanvas(boolean adjustPosition) {
        if (!navigating) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;

            Collection<VisWindow> children = boxWindows.values();
            if (children.size() == 0) {
                minX = 0;
                minY = 0;
                maxX = 0;
                maxY = 0;
            } else {
                for (Actor child : children) {
                    float childX = child.getX();
                    float childY = child.getY();
                    float childWidth = child.getWidth();
                    float childHeight = child.getHeight();
                    minX = Math.min(minX, childX);
                    minY = Math.min(minY, childY);
                    maxX = Math.max(maxX, childX + childWidth);
                    maxY = Math.max(maxY, childY + childHeight);
                }
            }

            minX -= CANVAS_GAP;
            minY -= CANVAS_GAP;
            maxX += CANVAS_GAP;
            maxY += CANVAS_GAP;

            canvasWidth = maxX - minX;
            canvasHeight = maxY - minY;

            if (adjustPosition) {
                canvasX = -minX;
                canvasY = -minY;
            }
        }
    }

    public void adjustCanvas() {
        updateCanvas(true);
    }

    public void setValidationResult(GraphValidator.ValidationResult<GraphBox, GraphConnection, PropertyBox> validationResult) {
        this.validationResult = validationResult;
        for (GraphBox value : graphBoxes.values()) {
            VisWindow window = boxWindows.get(value.getId());
            if (validationResult.getErrorNodes().contains(value)) {
                window.getTitleLabel().setColor(INVALID_LABEL_COLOR);
            } else if (validationResult.getWarningNodes().contains(value)) {
                window.getTitleLabel().setColor(WARNING_LABEL_COLOR);
            } else {
                window.getTitleLabel().setColor(VALID_LABEL_COLOR);
            }
        }
    }

    public GraphValidator.ValidationResult<GraphBox, GraphConnection, PropertyBox> getValidationResult() {
        return validationResult;
    }


    private void processRightClick(float x, float y) {
        if (!containedInWindow(x, y)) {
            NodeGroupImpl nodeGroup = null;
            for (Map.Entry<NodeGroupImpl, Rectangle> nodeGroupEntry : nodeGroups.entrySet()) {
                Rectangle rectangle = nodeGroupEntry.getValue();
                if (rectangle.contains(x, y) && y > rectangle.y + rectangle.height - GROUP_LABEL_HEIGHT) {
                    // Hit the label
                    nodeGroup = nodeGroupEntry.getKey();
                    break;
                }
            }
            if (nodeGroup != null) {
                final NodeGroupImpl finalNodeGroup = nodeGroup;

                PopupMenu popupMenu = new PopupMenu();
                MenuItem rename = new MenuItem("Rename group");
                rename.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                Dialogs.showInputDialog(getStage(), "Enter group name", "Name",
                                        new InputValidator() {
                                            @Override
                                            public boolean validateInput(String input) {
                                                return !input.trim().isEmpty();
                                            }
                                        },
                                        new InputDialogListener() {
                                            @Override
                                            public void finished(String input) {
                                                finalNodeGroup.setName(input.trim());
                                                fire(new GraphChangedEvent(false, false));
                                            }

                                            @Override
                                            public void canceled() {

                                            }
                                        });
                            }
                        });
                popupMenu.addItem(rename);

                MenuItem remove = new MenuItem("Remove group");
                remove.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                nodeGroups.remove(finalNodeGroup);
                                fire(new GraphChangedEvent(false, false));
                            }
                        });
                popupMenu.addItem(remove);

                popupMenu.showMenu(getStage(), x + getX(), y + getY());
            } else {
                PopupMenu popupMenu = popupMenuProducer.createPopupMenu(x, y);
                popupMenu.showMenu(getStage(), x + getX(), y + getY());
            }
        }
    }

    private void processLeftClick(float x, float y) {
        if (containedInWindow(x, y))
            return;

        for (Map.Entry<NodeConnector, Shape> nodeEntry : connectionNodeMap.entrySet()) {
            if (nodeEntry.getValue().contains(x, y)) {
                processNodeClick(nodeEntry.getKey());
                return;
            }
        }

        for (Map.Entry<GraphConnection, Shape> connectionEntry : connections.entrySet()) {
            if (connectionEntry.getValue().contains(x, y)) {
                GraphConnection connection = connectionEntry.getKey();
                removeConnection(connection);
                return;
            }
        }

        drawingFromConnector = null;
    }

    private boolean containedInWindow(float x, float y) {
        for (VisWindow window : boxWindows.values()) {
            float x1 = window.getX();
            float y1 = window.getY();
            float width = window.getWidth();
            float height = window.getHeight();
            // If window contains it - return
            if (x >= x1 && x < x1 + width
                    && y >= y1 && y < y1 + height)
                return true;
        }
        return false;
    }

    private void removeConnection(GraphConnection connection) {
        graphConnections.remove(connection);
        fire(new GraphChangedEvent(true, false));
        invalidate();
    }

    private void processNodeClick(NodeConnector clickedNodeConnector) {
        GraphBox clickedNode = getGraphBoxById(clickedNodeConnector.getNodeId());
        if (drawingFromConnector != null) {
            if (!drawingFromConnector.equals(clickedNodeConnector)) {
                GraphBox drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());

                boolean drawingFromIsInput = drawingFromNode.getConfiguration().getNodeInputs().containsKey(drawingFromConnector.getFieldId());
                if (drawingFromIsInput == clickedNode.getConfiguration().getNodeInputs().containsKey(clickedNodeConnector.getFieldId())) {
                    drawingFromConnector = null;
                } else {
                    NodeConnector connectorFrom = drawingFromIsInput ? clickedNodeConnector : drawingFromConnector;
                    NodeConnector connectorTo = drawingFromIsInput ? drawingFromConnector : clickedNodeConnector;

                    GraphNodeOutput output = getGraphBoxById(connectorFrom.getNodeId()).getConfiguration().getNodeOutputs().get(connectorFrom.getFieldId());
                    GraphNodeInput input = getGraphBoxById(connectorTo.getNodeId()).getConfiguration().getNodeInputs().get(connectorTo.getFieldId());

                    if (!connectorsMatch(input, output)) {
                        // Either input-input, output-output, or different property type
                        drawingFromConnector = null;
                    } else {
                        // Remove conflicting connections if needed
                        if (!input.isAcceptingMultiple()) {
                            for (GraphConnection oldConnection : findNodeConnections(connectorTo)) {
                                removeConnection(oldConnection);
                            }
                        }
                        if (!output.supportsMultiple()) {
                            for (GraphConnection oldConnection : findNodeConnections(connectorFrom)) {
                                removeConnection(oldConnection);
                            }
                        }
                        addGraphConnection(connectorFrom.getNodeId(), connectorFrom.getFieldId(),
                                connectorTo.getNodeId(), connectorTo.getFieldId());
                        drawingFromConnector = null;
                    }
                }
            } else {
                // Same node, that started at
                drawingFromConnector = null;
            }
        } else {
            boolean input = clickedNode.getConfiguration().getNodeInputs().containsKey(clickedNodeConnector.getFieldId());
            if ((input && !clickedNode.getConfiguration().getNodeInputs().get(clickedNodeConnector.getFieldId()).isAcceptingMultiple())
                    || (!input && !clickedNode.getConfiguration().getNodeOutputs().get(clickedNodeConnector.getFieldId()).supportsMultiple())) {
                List<GraphConnection> nodeConnections = findNodeConnections(clickedNodeConnector);
                if (nodeConnections.size() > 0) {
                    GraphConnection oldConnection = nodeConnections.get(0);
                    removeConnection(oldConnection);
                    NodeConnector oldNode = getNodeInfo(oldConnection.getNodeFrom(), oldConnection.getFieldFrom());
                    if (oldNode.equals(clickedNodeConnector))
                        drawingFromConnector = getNodeInfo(oldConnection.getNodeTo(), oldConnection.getFieldTo());
                    else
                        drawingFromConnector = oldNode;
                } else {
                    drawingFromConnector = clickedNodeConnector;
                }
            } else {
                drawingFromConnector = clickedNodeConnector;
            }
        }
    }

    private boolean connectorsMatch(GraphNodeInput input, GraphNodeOutput output) {
        Array<String> producablePropertyTypes = output.getProducableFieldTypes();
        for (String acceptedPropertyType : input.getAcceptedPropertyTypes()) {
            if (producablePropertyTypes.contains(acceptedPropertyType, false))
                return true;
        }

        return false;
    }

    private List<GraphConnection> findNodeConnections(NodeConnector nodeConnector) {
        String nodeId = nodeConnector.getNodeId();
        String fieldId = nodeConnector.getFieldId();

        List<GraphConnection> result = new LinkedList<>();
        for (GraphConnection graphConnection : graphConnections) {
            if ((graphConnection.getNodeFrom().equals(nodeId) && graphConnection.getFieldFrom().equals(fieldId))
                    || (graphConnection.getNodeTo().equals(nodeId) && graphConnection.getFieldTo().equals(fieldId)))
                result.add(graphConnection);
        }
        return result;
    }

    public void addGraphBox(final GraphBox graphBox, String windowTitle, boolean closeable, float x, float y) {
        graphBoxes.put(graphBox.getId(), graphBox);
        VisWindow window = new VisWindow(windowTitle, false) {
            @Override
            protected void positionChanged() {
                graphWindowMoved(this, graphBox.getId());
            }

            @Override
            protected void close() {
                removeGraphBox(graphBox);
                windowPositions.remove(this);
                super.close();
            }

            @Override
            public void toFront() {
                super.toFront();
                String nodeId = graphBox.getId();
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    if (selectedNodes.contains(nodeId))
                        removeFromSelection(nodeId);
                    else
                        addToSelection(nodeId);
                } else {
                    setSelection(nodeId);
                }
            }
        };
        window.setKeepWithinStage(false);
        if (closeable) {
            window.addCloseButton();
        }
        window.add(graphBox.getActor()).grow().row();
        windowPositions.put(window, new Vector2(x, y));
        window.setPosition(x, y);
        addActor(window);
        window.setSize(Math.max(150, window.getPrefWidth()), window.getPrefHeight());
        boxWindows.put(graphBox.getId(), window);
        fire(new GraphChangedEvent(true, false));
    }

    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        nodeGroups.put(new NodeGroupImpl(name, nodeIds), new Rectangle());
        updateNodeGroups();
        fire(new GraphChangedEvent(false, false));
    }

    private void graphWindowMoved(VisWindow visWindow, String nodeId) {
        if (!movingSelected && !navigating) {
            movingSelected = true;
            Vector2 oldPosition = windowPositions.get(visWindow);
            float movedX = visWindow.getX() - oldPosition.x;
            float movedY = visWindow.getY() - oldPosition.y;
            for (String selectedNode : selectedNodes) {
                if (!selectedNode.equals(nodeId)) {
                    boxWindows.get(selectedNode).moveBy(movedX, movedY);
                }
            }

            windowsMoved();
            movingSelected = false;
        }
        windowPositions.get(visWindow).set(visWindow.getX(), visWindow.getY());
    }

    private void windowsMoved() {
        recreateClickableShapes();
        updateNodeGroups();
        updateCanvas(true);
        fire(new GraphChangedEvent(false, false));
    }

    private void removeFromSelection(String nodeId) {
        selectedNodes.remove(nodeId);
        updateSelectedVisuals();
    }

    private void addToSelection(String nodeId) {
        selectedNodes.add(nodeId);
        updateSelectedVisuals();
    }

    private void setSelection(String nodeId) {
        selectedNodes.clear();
        selectedNodes.add(nodeId);
        updateSelectedVisuals();
    }

    private void updateSelectedVisuals() {
        VisWindow.WindowStyle notSelectedStyle = VisUI.getSkin().get("noborder", VisWindow.WindowStyle.class);
        VisWindow.WindowStyle selectedStyle = VisUI.getSkin().get("default", VisWindow.WindowStyle.class);

        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            VisWindow.WindowStyle newStyle = selectedNodes.contains(windowEntry.getKey()) ? selectedStyle : notSelectedStyle;
            windowEntry.getValue().setStyle(newStyle);
        }
    }

    public ObjectSet<String> getSelectedNodes() {
        return selectedNodes;
    }

    private void removeGraphBox(GraphBox graphBox) {
        Iterator<GraphConnection> graphConnectionIterator = graphConnections.iterator();
        String nodeId = graphBox.getId();
        while (graphConnectionIterator.hasNext()) {
            GraphConnection graphConnectionImpl = graphConnectionIterator.next();
            if (graphConnectionImpl.getNodeFrom().equals(nodeId)
                    || graphConnectionImpl.getNodeTo().equals(nodeId))
                graphConnectionIterator.remove();
        }

        boxWindows.remove(nodeId);
        graphBoxes.remove(nodeId);
        selectedNodes.remove(nodeId);
        for (NodeGroupImpl nodeGroupImpl : nodeGroups.keySet()) {
            if (nodeGroupImpl.getNodeIds().remove(nodeId)) {
                if (nodeGroupImpl.getNodeIds().size == 0) {
                    nodeGroups.remove(nodeGroupImpl);
                }
                break;
            }
        }

        graphBox.dispose();

        fire(new GraphChangedEvent(true, false));
    }

    public void addGraphConnection(String fromNode, String fromField, String toNode, String toField) {
        NodeConnector nodeFrom = getNodeInfo(fromNode, fromField);
        NodeConnector nodeTo = getNodeInfo(toNode, toField);
        if (nodeFrom == null)
            throw new IllegalArgumentException("Can't find connector, id: " + fromNode + ", field: " + fromField);
        if (nodeTo == null)
            throw new IllegalArgumentException("Can't find connector, id: " + toNode + ", field: " + toField);
        graphConnections.add(new GraphConnectionImpl(fromNode, fromField, toNode, toField));
        fire(new GraphChangedEvent(true, false));
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        updateShadeRenderer();
        recreateClickableShapes();
        updateNodeGroups();
        updateCanvas(false);
    }

    private void updateNodeGroups() {
        for (Map.Entry<NodeGroupImpl, Rectangle> nodeGroupEntry : nodeGroups.entrySet()) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            NodeGroupImpl nodeGroupImpl = nodeGroupEntry.getKey();
            for (String nodeId : nodeGroupImpl.getNodeIds()) {
                VisWindow window = boxWindows.get(nodeId);
                if (window == null)
                    throw new IllegalStateException("Unable to find node with id: " + nodeId);
                float windowX = window.getX();
                float windowY = window.getY();
                float windowWidth = window.getWidth();
                float windowHeight = window.getHeight();
                minX = Math.min(minX, windowX);
                minY = Math.min(minY, windowY);
                maxX = Math.max(maxX, windowX + windowWidth);
                maxY = Math.max(maxY, windowY + windowHeight);
            }

            minX -= GROUP_GAP;
            minY -= GROUP_GAP;
            maxX += GROUP_GAP;
            maxY += GROUP_GAP + GROUP_LABEL_HEIGHT;
            nodeGroupEntry.getValue().set(minX, minY, maxX - minX, maxY - minY);
        }
    }

    private void recreateClickableShapes() {
        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            VisWindow window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
            float windowX = window.getX();
            float windowY = window.getY();
            for (GraphBoxInputConnector connector : graphBox.getInputs().values()) {
                switch (connector.getSide()) {
                    case Left:
                        from.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                        break;
                    case Top:
                        from.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - CONNECTOR_RADIUS, from.y - CONNECTOR_RADIUS,
                        CONNECTOR_RADIUS * 2, CONNECTOR_RADIUS * 2);

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
            }
            for (GraphBoxOutputConnector connector : graphBox.getOutputs().values()) {
                switch (connector.getSide()) {
                    case Right:
                        from.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                        break;
                    case Bottom:
                        from.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - CONNECTOR_RADIUS, from.y - CONNECTOR_RADIUS,
                        CONNECTOR_RADIUS * 2, CONNECTOR_RADIUS * 2);

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
            }
        }

        BasicStroke basicStroke = new BasicStroke(7);
        Vector2 to = new Vector2();
        for (GraphConnection graphConnection : graphConnections) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            VisWindow fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector output = getGraphBoxById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            VisWindow toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector input = getGraphBoxById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);
            Shape shape;
            if (output.getSide() == GraphBoxOutputConnector.Side.Right) {
                shape = basicStroke.createStrokedShape(new CubicCurve2D.Float(from.x, from.y, ((from.x + to.x) / 2), from.y, ((from.x + to.x) / 2), to.y, to.x, to.y));
            } else {
                shape = basicStroke.createStrokedShape(new CubicCurve2D.Float(from.x, from.y, from.x, ((from.y + to.y) / 2), to.x, ((from.y + to.y) / 2), to.x, to.y));
            }
            connections.put(graphConnection, shape);
        }
    }

    private NodeConnector getNodeInfo(String nodeId, String fieldId) {
        GraphBox graphBox = graphBoxes.get(nodeId);
        if (graphBox.getInputs().get(fieldId) != null || graphBox.getOutputs().get(fieldId) != null)
            return new NodeConnector(nodeId, fieldId);
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();
        batch.end();
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        drawGroups(batch);
        drawConnections();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void drawGroups(Batch batch) {
        if (!nodeGroups.isEmpty()) {
            float x = getX();
            float y = getY();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(GROUP_BACKGROUND_COLOR);
            for (Map.Entry<NodeGroupImpl, Rectangle> nodeGroupEntry : nodeGroups.entrySet()) {
                Rectangle rectangle = nodeGroupEntry.getValue();
                shapeRenderer.rect(x + rectangle.x, y + rectangle.y, rectangle.width, rectangle.height);
            }
            shapeRenderer.end();

            BitmapFont font = skin.getFont("default-font");
            batch.begin();
            for (Map.Entry<NodeGroupImpl, Rectangle> nodeGroupEntry : nodeGroups.entrySet()) {
                NodeGroupImpl nodeGroupImpl = nodeGroupEntry.getKey();
                Rectangle rectangle = nodeGroupEntry.getValue();
                String name = nodeGroupImpl.getName();
                font.draw(batch, name, x + rectangle.x + NODE_GROUP_PADDING, y + rectangle.y + rectangle.height - NODE_GROUP_PADDING,
                        0, name.length(), rectangle.width - NODE_GROUP_PADDING * 2, Align.center, false, "...");
            }
            batch.end();
        }
    }

    private void drawConnections() {
        float x = getX();
        float y = getY();

        Vector2 from = new Vector2();
        Vector2 to = new Vector2();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(LINE_COLOR);

        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            VisWindow window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
            for (GraphNodeInput connector : graphBox.getConfiguration().getNodeInputs().values()) {
                if (!connector.isRequired()) {
                    String fieldId = connector.getFieldId();
                    calculateConnector(from, to, window, graphBox.getInputs().get(fieldId));
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
            }

            for (GraphBoxOutputConnector connector : graphBox.getOutputs().values()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnection graphConnection : graphConnections) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            VisWindow fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector output = getGraphBoxById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            VisWindow toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector input = getGraphBoxById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            boolean error = validationResult.getErrorConnections().contains(graphConnection);
            shapeRenderer.setColor(error ? INVALID_CONNECTOR_COLOR : VALID_CONNECTOR_COLOR);

            from.add(x, y);
            to.add(x, y);
            if (output.getSide() == GraphBoxOutputConnector.Side.Right) {
                shapeRenderer.curve(from.x, from.y, ((from.x + to.x) / 2), from.y, ((from.x + to.x) / 2), to.y, to.x, to.y, 100);
            } else {
                shapeRenderer.curve(from.x, from.y, from.x, ((from.y + to.y) / 2), to.x, ((from.y + to.y) / 2), to.x, to.y, 100);
            }

        }

        if (drawingFromConnector != null) {
            shapeRenderer.setColor(LINE_COLOR);
            GraphBox drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());
            VisWindow fromWindow = getBoxWindow(drawingFromConnector.getNodeId());
            if (drawingFromNode.getConfiguration().getNodeInputs().containsKey(drawingFromConnector.getFieldId())) {
                GraphBoxInputConnector input = drawingFromNode.getInputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, input);
                Vector2 mouseLocation = getStage().getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                if (input.getSide() == GraphBoxInputConnector.Side.Left) {
                    shapeRenderer.curve(x + from.x, y + from.y, ((x + from.x + mouseLocation.x) / 2), y + from.y,
                            ((x + from.x + mouseLocation.x) / 2), mouseLocation.y, mouseLocation.x, mouseLocation.y, 100);
                } else {
                    shapeRenderer.curve(x + from.x, y + from.y, x + from.x, ((y + from.y + mouseLocation.y) / 2),
                            mouseLocation.x, ((y + from.y + mouseLocation.y) / 2), mouseLocation.x, mouseLocation.y, 100);
                }
            } else {
                GraphBoxOutputConnector output = drawingFromNode.getOutputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, output);
                Vector2 mouseLocation = getStage().getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                if (output.getSide() == GraphBoxOutputConnector.Side.Right) {
                    shapeRenderer.curve(x + from.x, y + from.y, ((x + from.x + mouseLocation.x) / 2), y + from.y,
                            ((x + from.x + mouseLocation.x) / 2), mouseLocation.y, mouseLocation.x, mouseLocation.y, 100);
                } else {
                    shapeRenderer.curve(x + from.x, y + from.y, x + from.x, ((y + from.y + mouseLocation.y) / 2),
                            mouseLocation.x, ((y + from.y + mouseLocation.y) / 2), mouseLocation.x, mouseLocation.y, 100);
                }
            }
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            VisWindow window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
            for (GraphNodeInput connector : graphBox.getConfiguration().getNodeInputs().values()) {
                if (connector.isRequired()) {
                    String fieldId = connector.getFieldId();
                    calculateConnector(from, to, window, graphBox.getInputs().get(fieldId));
                    from.add(x, y);
                    to.add(x, y);

                    boolean isErrorous = false;
                    for (NodeConnector errorConnector : validationResult.getErrorConnectors()) {
                        if (errorConnector.getNodeId().equals(nodeId) && errorConnector.getFieldId().equals(connector.getFieldId())) {
                            isErrorous = true;
                            break;
                        }
                    }
                    shapeRenderer.setColor(isErrorous ? INVALID_CONNECTOR_COLOR : VALID_CONNECTOR_COLOR);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
            }
        }
        shapeRenderer.end();
    }

    private void calculateConnector(Vector2 from, Vector2 to, VisWindow window, GraphBoxOutputConnector connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                from.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                to.set(windowX + window.getWidth(), windowY + connector.getOffset());
                break;
            case Bottom:
                from.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                to.set(windowX + connector.getOffset(), windowY);
                break;
        }
    }

    private void calculateConnector(Vector2 from, Vector2 to, VisWindow window, GraphBoxInputConnector connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                from.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                to.set(windowX, windowY + connector.getOffset());
                break;
            case Top:
                from.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                to.set(windowX + connector.getOffset(), windowY + window.getHeight());
                break;
        }
    }

    public GraphBox getGraphBoxById(String id) {
        return graphBoxes.get(id);
    }

    public Iterable<GraphBox> getGraphBoxes() {
        return graphBoxes.values();
    }

    public Iterable<? extends GraphConnection> getConnections() {
        return graphConnections;
    }

    public Iterable<? extends NodeGroup> getNodeGroups() {
        return nodeGroups.keySet();
    }

    private void updateShadeRenderer() {
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.updateMatrices();
    }

    private void calculateConnection(Vector2 position, VisWindow window, GraphBoxInputConnector connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                position.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                break;
            case Top:
                position.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                break;
        }
    }

    private void calculateConnection(Vector2 position, VisWindow window, GraphBoxOutputConnector connector) {
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                position.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                break;
            case Bottom:
                position.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                break;
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
        for (GraphBox graphBox : graphBoxes.values()) {
            graphBox.dispose();
        }
    }

    public VisWindow getBoxWindow(String nodeId) {
        return boxWindows.get(nodeId);
    }

    public void createNodeGroup() {
        if (selectedNodes.size > 0) {
            for (String selectedNode : selectedNodes) {
                if (groupsContain(selectedNode))
                    return;
            }

            Dialogs.showInputDialog(getStage(), "Enter group name", "Name",
                    new InputValidator() {
                        @Override
                        public boolean validateInput(String input) {
                            return !input.trim().isEmpty();
                        }
                    },
                    new InputDialogListener() {
                        @Override
                        public void finished(String input) {
                            addNodeGroup(input.trim(), new ObjectSet<String>(selectedNodes));
                        }

                        @Override
                        public void canceled() {

                        }
                    });
        }
    }

    private boolean groupsContain(String selectedNode) {
        for (NodeGroupImpl nodeGroupImpl : nodeGroups.keySet()) {
            if (nodeGroupImpl.getNodeIds().contains(selectedNode))
                return true;
        }
        return false;
    }

    private static class NodeGroupImpl implements NodeGroup {
        private String name;
        private ObjectSet<String> nodes;

        public NodeGroupImpl(String name, ObjectSet<String> nodes) {
            this.name = name;
            this.nodes = nodes;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public ObjectSet<String> getNodeIds() {
            return nodes;
        }
    }
}

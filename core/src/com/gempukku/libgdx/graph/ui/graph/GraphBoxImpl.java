package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.*;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GraphBoxImpl implements GraphBox {
    private String id;
    private NodeConfiguration configuration;
    private VisTable table;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxInputConnector> inputConnectors = new HashMap<>();
    private Map<String, GraphBoxOutputConnector> outputConnectors = new HashMap<>();

    public GraphBoxImpl(String id, NodeConfiguration configuration) {
        this.id = id;
        this.configuration = configuration;
        table = new VisTable();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {

    }

    @Override
    public NodeConfiguration getConfiguration() {
        return configuration;
    }

    public void addTopConnector(GraphNodeInput graphNodeInput) {
        inputConnectors.put(graphNodeInput.getFieldId(), new GraphBoxInputConnectorImpl(GraphBoxInputConnector.Side.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, graphNodeInput.getFieldId()));
    }

    public void addBottomConnector(GraphNodeOutput graphNodeOutput) {
        outputConnectors.put(graphNodeOutput.getFieldId(), new GraphBoxOutputConnectorImpl(GraphBoxOutputConnector.Side.Bottom,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return table.getWidth() / 2f;
                    }
                }, graphNodeOutput.getFieldId()));
    }

    public void addTwoSideGraphPart(GraphNodeInput graphNodeInput,
                                    GraphNodeOutput graphNodeOutput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName())).grow();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName());
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, graphNodeInput);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(GraphNodeInput graphNodeInput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName())).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, graphNodeInput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            GraphNodeOutput graphNodeOutput) {
        VisTable table = new VisTable();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName());
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphBoxPart graphBoxPart) {
        graphBoxParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
        final GraphBoxInputConnector inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new GraphBoxInputConnectorImpl(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getFieldId()));
        }
        final GraphBoxOutputConnector outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new GraphBoxOutputConnectorImpl(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector.getFieldId()));
        }
    }

    @Override
    public Map<String, GraphBoxInputConnector> getInputs() {
        return inputConnectors;
    }

    @Override
    public Map<String, GraphBoxOutputConnector> getOutputs() {
        return outputConnectors;
    }

    @Override
    public Actor getActor() {
        return table;
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphBoxPart graphBoxPart : graphBoxParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    @Override
    public void dispose() {
        for (GraphBoxPart part : graphBoxParts) {
            part.dispose();
        }
    }
}

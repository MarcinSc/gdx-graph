package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.*;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GdxGraphNodeEditor implements GraphNodeEditor, Disposable, GraphChangedAware {
    private static final String ioLabelStyle = "gdx-graph-io-label";

    private NodeConfiguration configuration;
    private VisTable table;
    private List<GraphNodeEditorPart> editorParts = new LinkedList<>();
    private Map<String, GraphNodeEditorInput> inputConnectors = new HashMap<>();
    private Map<String, GraphNodeEditorOutput> outputConnectors = new HashMap<>();

    public GdxGraphNodeEditor(NodeConfiguration configuration) {
        this.configuration = configuration;
        table = new VisTable();
    }

    @Override
    public NodeConfiguration getConfiguration() {
        return configuration;
    }

    public void addTopConnector(GraphNodeInput graphNodeInput) {
        inputConnectors.put(graphNodeInput.getFieldId(), new DefaultGraphNodeEditorInput(GraphNodeEditorInput.Side.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, graphNodeInput.getFieldId()));
    }

    public void addBottomConnector(GraphNodeOutput graphNodeOutput) {
        outputConnectors.put(graphNodeOutput.getFieldId(), new DefaultGraphNodeEditorOutput(GraphNodeEditorOutput.Side.Bottom,
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
        table.add(new VisLabel(graphNodeInput.getFieldName(), ioLabelStyle)).grow();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), ioLabelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setInputConnector(GraphNodeEditorInput.Side.Left, graphNodeInput);
        graphBoxPart.setOutputConnector(GraphNodeEditorOutput.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(GraphNodeInput graphNodeInput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName(), ioLabelStyle)).grow().row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setInputConnector(GraphNodeEditorInput.Side.Left, graphNodeInput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            GraphNodeOutput graphNodeOutput) {
        VisTable table = new VisTable();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), ioLabelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setOutputConnector(GraphNodeEditorOutput.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphNodeEditorPart graphBoxPart) {
        editorParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
        final GraphNodeEditorInput inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new DefaultGraphNodeEditorInput(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getFieldId()));
        }
        final GraphNodeEditorOutput outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new DefaultGraphNodeEditorOutput(outputConnector.getSide(),
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
    public Map<String, GraphNodeEditorInput> getInputs() {
        return inputConnectors;
    }

    @Override
    public Map<String, GraphNodeEditorOutput> getOutputs() {
        return outputConnectors;
    }

    @Override
    public Actor getActor() {
        return table;
    }

    public void initialize(JsonValue data) {
        for (GraphNodeEditorPart editorPart : editorParts) {
            editorPart.initialize(data);
        }
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphNodeEditorPart graphBoxPart : editorParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        for (GraphNodeEditorPart editorPart : editorParts) {
            if (editorPart instanceof GraphChangedAware) {
                ((GraphChangedAware) editorPart).graphChanged(event, hasErrors, graph);
            }
        }
    }

    @Override
    public void dispose() {
        for (GraphNodeEditorPart editorPart : editorParts) {
            if (editorPart instanceof Disposable) {
                ((Disposable) editorPart).dispose();
            }
        }
    }
}

package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.editor.*;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GdxGraphNodeEditor extends VisTable implements GraphNodeEditor, GraphChangedAware {
    private static final String ioLabelStyle = "gdx-graph-io-label";

    private String nodeId;
    private NodeConfiguration configuration;
    private List<GraphNodeEditorPart> editorParts = new LinkedList<>();
    private Map<String, GraphNodeEditorInput> inputConnectors = new HashMap<>();
    private Map<String, GraphNodeEditorOutput> outputConnectors = new HashMap<>();

    public GdxGraphNodeEditor(String nodeId, NodeConfiguration configuration) {
        this.nodeId = nodeId;
        this.configuration = configuration;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public NodeConfiguration getConfiguration() {
        return configuration;
    }

    public Drawable getInputDrawable(GraphNodeInput input, boolean valid) {
        boolean required = input.isRequired();
        String side = (input.getSide() == GraphNodeInputSide.Left) ? "left" : "top";
        String drawable = "connector-" + side + (required ? "-required" : "") + (valid ? "" : "-invalid");
        return VisUI.getSkin().getDrawable(drawable);
    }

    public Drawable getOutputDrawable(GraphNodeOutput output, boolean valid) {
        String side = (output.getSide() == GraphNodeOutputSide.Right) ? "right" : "bottom";
        String drawable = "connector-" + side + (valid ? "" : "-invalid");
        return VisUI.getSkin().getDrawable(drawable);
    }

    public void addTopConnector(GraphNodeInput graphNodeInput) {
        boolean required = graphNodeInput.isRequired();
        String drawable = "connector-top" + (required ? "-required" : "");
        inputConnectors.put(graphNodeInput.getFieldId(), new DefaultGraphNodeEditorInput(GraphNodeInputSide.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return getWidth() / 2f;
            }
        }, graphNodeInput.getFieldId(), getInputDrawable(graphNodeInput, true), getInputDrawable(graphNodeInput, false)));
    }

    public void addBottomConnector(GraphNodeOutput graphNodeOutput) {
        String drawable = "connector-bottom";
        outputConnectors.put(graphNodeOutput.getFieldId(), new DefaultGraphNodeEditorOutput(GraphNodeOutputSide.Bottom,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return getWidth() / 2f;
                    }
                }, graphNodeOutput.getFieldId(), getOutputDrawable(graphNodeOutput, true), getOutputDrawable(graphNodeOutput, false)));
    }

    public void addTwoSideGraphPart(GraphNodeInput graphNodeInput,
                                    GraphNodeOutput graphNodeOutput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName(), ioLabelStyle)).grow();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), ioLabelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        DefaultGraphNodeEditorPart graphEditorPart = new DefaultGraphNodeEditorPart(table, null);
        graphEditorPart.setInputConnector(GraphNodeInputSide.Left, graphNodeInput, getInputDrawable(graphNodeInput, true), getInputDrawable(graphNodeInput, false));
        graphEditorPart.setOutputConnector(GraphNodeOutputSide.Right, graphNodeOutput, getOutputDrawable(graphNodeOutput, true), getOutputDrawable(graphNodeOutput, false));
        addGraphEditorPart(graphEditorPart);
    }

    public void addInputGraphPart(GraphNodeInput graphNodeInput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName(), ioLabelStyle)).grow().row();

        DefaultGraphNodeEditorPart graphEditorPart = new DefaultGraphNodeEditorPart(table, null);
        graphEditorPart.setInputConnector(GraphNodeInputSide.Left, graphNodeInput, getInputDrawable(graphNodeInput, true), getInputDrawable(graphNodeInput, false));
        addGraphEditorPart(graphEditorPart);
    }

    public void addOutputGraphPart(
            GraphNodeOutput graphNodeOutput) {
        VisTable table = new VisTable();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), ioLabelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        DefaultGraphNodeEditorPart graphEditorPart = new DefaultGraphNodeEditorPart(table, null);
        graphEditorPart.setOutputConnector(GraphNodeOutputSide.Right, graphNodeOutput, getOutputDrawable(graphNodeOutput, true), getOutputDrawable(graphNodeOutput, false));
        addGraphEditorPart(graphEditorPart);
    }

    public void addGraphEditorPart(GraphNodeEditorPart graphEditorPart) {
        editorParts.add(graphEditorPart);
        final Actor actor = graphEditorPart.getActor();
        add(actor).growX().row();
        final GraphNodeEditorInput inputConnector = graphEditorPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new DefaultGraphNodeEditorInput(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getFieldId(), inputConnector.getConnectorDrawable(true), inputConnector.getConnectorDrawable(false)));
        }
        final GraphNodeEditorOutput outputConnector = graphEditorPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new DefaultGraphNodeEditorOutput(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector.getFieldId(), outputConnector.getConnectorDrawable(true), outputConnector.getConnectorDrawable(false)));
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
        return this;
    }

    public void initialize(JsonValue data) {
        for (GraphNodeEditorPart editorPart : editorParts) {
            editorPart.initialize(data);
        }
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphNodeEditorPart graphEditorPart : editorParts)
            graphEditorPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    @Override
    public void graphChanged(GraphChangedEvent event, GraphWithProperties graph) {
        for (GraphNodeEditorPart editorPart : editorParts) {
            if (editorPart instanceof GraphChangedAware) {
                ((GraphChangedAware) editorPart).graphChanged(event, graph);
            }
        }
    }
}

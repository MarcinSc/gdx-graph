package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.data.impl.NamedGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.NamedGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.impl.NamedNodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;

import java.util.Iterator;

public class GdxGraphNodeEditorProducer implements MenuGraphNodeEditorProducer {
    private MenuNodeConfiguration configuration;
    private boolean closeable = true;

    public GdxGraphNodeEditorProducer(MenuNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public boolean isCloseable() {
        return closeable;
    }

    @Override
    public GraphNodeEditor createNodeEditor(String nodeId, JsonValue data) {
        GdxGraphNodeEditor nodeEditor = new GdxGraphNodeEditor(nodeId, configuration);
        buildNodeEditorBeforeIO(nodeEditor, configuration);
        addConfigurationInputsAndOutputs(nodeEditor);
        buildNodeEditorAfterIO(nodeEditor, configuration);

        if (data != null)
            nodeEditor.initialize(data);

        return nodeEditor;
    }

    protected void buildNodeEditorBeforeIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {

    }

    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {

    }

    protected boolean skipFieldId(String fieldId) {
        return false;
    }

    protected void addConfigurationInputsAndOutputs(GdxGraphNodeEditor nodeEditor) {
        addTopAndBottomConnectors(nodeEditor);
        addLeftAndRightConnectors(nodeEditor);
    }

    private void addTopAndBottomConnectors(GdxGraphNodeEditor nodeEditor) {
        for (GraphNodeInput input : configuration.getNodeInputs().values()) {
            if (input.getSide() == GraphNodeInputSide.Top && !skipFieldId(input.getFieldId())) {
                nodeEditor.addTopConnector(input);
            }
        }
        for (GraphNodeOutput output : configuration.getNodeOutputs().values()) {
            if (output.getSide() == GraphNodeOutputSide.Bottom && !skipFieldId(output.getFieldId())) {
                nodeEditor.addBottomConnector(output);
            }
        }
    }

    private void addLeftAndRightConnectors(GdxGraphNodeEditor nodeEditor) {
        if (configuration instanceof NamedNodeConfiguration) {
            NamedNodeConfiguration namedNodeConfiguration = (NamedNodeConfiguration) configuration;
            Iterator<? extends NamedGraphNodeInput> inputIterator = namedNodeConfiguration.getNodeInputs().values().iterator();
            Iterator<? extends NamedGraphNodeOutput> outputIterator = namedNodeConfiguration.getNodeOutputs().values().iterator();
            while (inputIterator.hasNext() || outputIterator.hasNext()) {
                NamedGraphNodeInput input = getNextLeftInput(inputIterator);
                NamedGraphNodeOutput output = getNextRightOutput(outputIterator);

                if (input != null && output != null) {
                    nodeEditor.addTwoSideGraphPart(input, output);
                } else if (input != null) {
                    nodeEditor.addInputGraphPart(input);
                } else if (output != null) {
                    nodeEditor.addOutputGraphPart(output);
                }
            }
        }
    }

    private NamedGraphNodeInput getNextLeftInput(Iterator<? extends NamedGraphNodeInput> inputIterator) {
        while (inputIterator.hasNext()) {
            NamedGraphNodeInput input = inputIterator.next();
            if (input.getSide() == GraphNodeInputSide.Left && !skipFieldId(input.getFieldId())) {
                return input;
            }
        }
        return null;
    }

    private NamedGraphNodeOutput getNextRightOutput(Iterator<? extends NamedGraphNodeOutput> outputIterator) {
        while (outputIterator.hasNext()) {
            NamedGraphNodeOutput output = outputIterator.next();
            if (output.getSide() == GraphNodeOutputSide.Right && !skipFieldId(output.getFieldId())) {
                return output;
            }
        }
        return null;
    }
}

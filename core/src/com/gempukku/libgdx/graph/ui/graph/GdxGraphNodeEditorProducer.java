package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;

import java.util.Iterator;

public class GdxGraphNodeEditorProducer implements MenuGraphNodeEditorProducer, GraphChangedAware {
    private MenuNodeConfiguration configuration;
    private ObjectSet<GraphChangedAware> awareChildren = new ObjectSet<>();
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
    public GraphNodeEditor createNodeEditor(Skin skin, JsonValue data) {
        GdxGraphNodeEditor nodeEditor = new GdxGraphNodeEditor(configuration) {
            @Override
            public void dispose() {
                super.dispose();
                awareChildren.remove(this);
            }
        };
        awareChildren.add(nodeEditor);
        buildNodeEditorBeforeIO(nodeEditor, skin, configuration);
        addConfigurationInputsAndOutputs(nodeEditor);
        buildNodeEditorAfterIO(nodeEditor, skin, configuration);

        if (data != null)
            nodeEditor.initialize(data);

        return nodeEditor;
    }

    protected void buildNodeEditorBeforeIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {

    }

    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {

    }

    protected void addConfigurationInputsAndOutputs(GdxGraphNodeEditor nodeEditor) {
        Iterator<GraphNodeInput> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput input = null;
            GraphNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    nodeEditor.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    nodeEditor.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                nodeEditor.addTwoSideGraphPart(input, output);
            } else if (input != null) {
                nodeEditor.addInputGraphPart(input);
            } else if (output != null) {
                nodeEditor.addOutputGraphPart(output);
            }
        }
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        for (GraphChangedAware awareChild : awareChildren) {
            awareChild.graphChanged(event, hasErrors, graph);
        }
    }
}

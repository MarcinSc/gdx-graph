package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;

import java.util.Iterator;

public class GraphBoxProducerImpl implements GraphBoxProducer {
    private NodeConfiguration configuration;

    public GraphBoxProducerImpl(NodeConfiguration configuration) {
        this.configuration = configuration;
    }

    public NodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl start = createGraphBox(id);
        addConfigurationInputsAndOutputs(start);

        return start;
    }

    protected GraphBoxImpl createGraphBox(String id) {
        return new GraphBoxImpl(id, configuration);
    }

    protected void addConfigurationInputsAndOutputs(GraphBoxImpl graphBox) {
        Iterator<GraphNodeInput> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput input = null;
            GraphNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    graphBox.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    graphBox.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                graphBox.addTwoSideGraphPart(input, output);
            } else if (input != null) {
                graphBox.addInputGraphPart(input);
            } else if (output != null) {
                graphBox.addOutputGraphPart(output);
            }
        }
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }
}

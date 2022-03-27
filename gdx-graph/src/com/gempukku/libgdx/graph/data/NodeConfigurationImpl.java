package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

public class NodeConfigurationImpl implements NodeConfiguration {
    private final String type;
    private final String name;
    private final String menuLocation;
    private final ObjectMap<String, GraphNodeInput> nodeInputs = new OrderedMap<>();
    private final ObjectMap<String, GraphNodeOutput> nodeOutputs = new OrderedMap<>();

    public NodeConfigurationImpl(String type, String name, String menuLocation) {
        this.type = type;
        this.name = name;
        this.menuLocation = menuLocation;
    }

    public void addNodeInput(GraphNodeInput input) {
        nodeInputs.put(input.getFieldId(), input);
    }

    public void addNodeOutput(GraphNodeOutput output) {
        nodeOutputs.put(output.getFieldId(), output);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMenuLocation() {
        return menuLocation;
    }

    @Override
    public ObjectMap<String, GraphNodeInput> getNodeInputs() {
        return nodeInputs;
    }

    @Override
    public ObjectMap<String, GraphNodeOutput> getNodeOutputs() {
        return nodeOutputs;
    }

    @Override
    public boolean isValid(ObjectMap<String, Array<String>> inputTypes, Iterable<? extends GraphProperty> properties) {
        for (GraphNodeOutput nodeOutput : nodeOutputs.values()) {
            String output = nodeOutput.determineFieldType(inputTypes);
            if (output == null)
                return false;
        }

        return true;
    }
}

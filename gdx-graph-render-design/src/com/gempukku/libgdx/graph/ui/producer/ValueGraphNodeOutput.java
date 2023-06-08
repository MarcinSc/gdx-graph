package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public class ValueGraphNodeOutput implements GraphNodeOutput {
    private final String fieldName;
    private final String fieldType;

    public ValueGraphNodeOutput(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    @Override
    public GraphNodeOutputSide getSide() {
        return GraphNodeOutputSide.Right;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getFieldId() {
        return "value";
    }

    @Override
    public Array<String> getConnectableFieldTypes() {
        Array<String> result = new Array<>();
        result.add(fieldType);
        return result;
    }

    @Override
    public boolean acceptsMultipleConnections() {
        return true;
    }

    @Override
    public String determineFieldType(ObjectMap<String, Array<String>> inputs) {
        return fieldType;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}

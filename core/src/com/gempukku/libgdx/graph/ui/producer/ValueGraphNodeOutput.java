package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

public class ValueGraphNodeOutput implements GraphNodeOutput {
    private String fieldName;
    private String fieldType;

    public ValueGraphNodeOutput(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    @Override
    public boolean isMainConnection() {
        return false;
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
    public Array<String> getProducableFieldTypes() {
        Array<String> result = new Array<>();
        result.add(fieldType);
        return result;
    }

    @Override
    public boolean supportsMultiple() {
        return true;
    }

    @Override
    public String determineFieldType(ObjectMap<String, Array<String>> inputs) {
        return fieldType;
    }
}

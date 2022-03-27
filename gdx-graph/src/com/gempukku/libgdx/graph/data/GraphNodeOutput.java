package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public interface GraphNodeOutput {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    Array<String> getProducableFieldTypes();

    String determineFieldType(ObjectMap<String, Array<String>> inputs);

    boolean supportsMultiple();
}

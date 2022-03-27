package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;

public interface GraphNodeInput {
    boolean isRequired();

    boolean isAcceptingMultiple();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    boolean acceptsInputTypes(Array<String> inputTypes);

    Array<String> getAcceptedPropertyTypes();
}

package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.data.GraphNodeInput;

public class GraphNodeInputImpl implements GraphNodeInput {
    private final String id;
    private final String name;
    private final boolean acceptingMultiple;
    private final Array<String> acceptedTypes;
    private final boolean required;
    private final boolean mainConnection;

    public GraphNodeInputImpl(String id, String name, String... acceptedType) {
        this(id, name, false, acceptedType);
    }

    public GraphNodeInputImpl(String id, String name, boolean required, String... acceptedType) {
        this(id, name, required, false, acceptedType);
    }

    public GraphNodeInputImpl(String id, String name, boolean required, boolean mainConnection, String... acceptedType) {
        this(id, name, required, mainConnection, false, acceptedType);
    }

    public GraphNodeInputImpl(String id, String name, boolean required, boolean mainConnection, boolean acceptingMultiple, String... acceptedType) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.mainConnection = mainConnection;
        this.acceptingMultiple = acceptingMultiple;
        this.acceptedTypes = new Array<>(acceptedType);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public boolean isAcceptingMultiple() {
        return acceptingMultiple;
    }

    @Override
    public Array<String> getAcceptedPropertyTypes() {
        return acceptedTypes;
    }

    @Override
    public boolean acceptsInputTypes(Array<String> inputTypes) {
        for (String inputType : inputTypes) {
            if (!acceptedTypes.contains(inputType, false))
                return false;
        }

        return true;
    }
}

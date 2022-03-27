package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

import java.util.function.Function;

public class GraphNodeOutputImpl implements GraphNodeOutput {
    private final String id;
    private final String name;
    private final boolean mainConnection;
    private final Function<ObjectMap<String, Array<String>>, String> outputTypeFunction;
    private final Array<String> propertyTypes;

    public GraphNodeOutputImpl(String id, String name, final String producedType) {
        this(id, name, false, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, final String producedType) {
        this(id, name, mainConnection, null, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, Function<ObjectMap<String, Array<String>>, String> outputTypeFunction, String... producedType) {
        this(id, name, false, outputTypeFunction, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, Function<ObjectMap<String, Array<String>>, String> outputTypeFunction, final String... producedType) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;
        if (outputTypeFunction == null) {
            outputTypeFunction = new Function<ObjectMap<String, Array<String>>, String>() {
                @Override
                public String apply(ObjectMap<String, Array<String>> stringTMap) {
                    return producedType[0];
                }
            };
        }
        this.outputTypeFunction = outputTypeFunction;
        this.propertyTypes = new Array<>(producedType);
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public Array<String> getProducableFieldTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }

    @Override
    public String determineFieldType(ObjectMap<String, Array<String>> inputs) {
        return outputTypeFunction.apply(inputs);
    }
}


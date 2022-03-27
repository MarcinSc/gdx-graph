package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Function;

public class ValidateSameTypeOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String fieldType;
    private final String[] inputs;

    public ValidateSameTypeOutputTypeFunction(String fieldType, String... input) {
        this.fieldType = fieldType;
        this.inputs = input;
    }

    @Override
    public String apply(ObjectMap<String, Array<String>> map) {
        String resolvedType = null;
        for (String input : inputs) {
            Array<String> type = map.get(input);
            if (type == null || type.size == 0)
                return null;
            if (resolvedType != null && !resolvedType.equals(type.get(0)))
                return null;
            resolvedType = type.get(0);
        }

        return fieldType;
    }
}

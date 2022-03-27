package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Function;

public class SameTypeOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String[] inputs;

    public SameTypeOutputTypeFunction(String... input) {
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

        return resolvedType;
    }
}

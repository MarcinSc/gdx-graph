package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;

/**
 * Validates that input is of the same type, but always returns the specified field type.
 */
public class ValidateSameTypeOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String fieldType;
    private final String[] inputs;

    public ValidateSameTypeOutputTypeFunction(String fieldType, String... input) {
        this.fieldType = fieldType;
        this.inputs = input;
    }

    @Override
    public String evaluate(ObjectMap<String, Array<String>> map) {
        String resolvedType = null;
        for (String input : inputs) {
            Array<String> types = map.get(input);
            if (types == null || types.size == 0)
                return null;
            for (String type : types) {
                if (resolvedType != null) {
                    if (!resolvedType.equals(type))
                        return null;
                } else {
                    resolvedType = type;
                }
            }
        }

        return fieldType;
    }
}

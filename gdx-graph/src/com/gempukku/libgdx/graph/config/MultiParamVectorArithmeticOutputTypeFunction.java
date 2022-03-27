package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Function;

public class MultiParamVectorArithmeticOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String floatType;
    private final String input;

    public MultiParamVectorArithmeticOutputTypeFunction(String floatType, String input) {
        this.floatType = floatType;
        this.input = input;
    }

    @Override
    public String apply(ObjectMap<String, Array<String>> inputs) {
        Array<String> types = inputs.get(input);
        if (types.size == 0 || types.get(0) == null)
            return null;

        String resultType = floatType;
        for (String type : types) {
            if (!type.equals(resultType) && (!resultType.equals(floatType) && !type.equals(floatType)))
                return null;
            if (!type.equals(floatType))
                resultType = type;
        }

        return resultType;
    }
}

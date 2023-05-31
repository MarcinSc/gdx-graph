package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;

/**
 * User for operations that accepts multiple values for the same input, however the values all have to have the same
 * type, or be of neutral type. Returns a non-neutral of those in the input (if found), or neutral if all inputs
 * were of neutral type.
 */
public class VectorArithmeticOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String neutralType;
    private final String[] inputs;

    public VectorArithmeticOutputTypeFunction(String neutralType, String... inputs) {
        this.neutralType = neutralType;
        this.inputs = inputs;
    }

    @Override
    public String evaluate(ObjectMap<String, Array<String>> map) {
        String resultType = neutralType;
        for (String input : inputs) {
            Array<String> types = map.get(input);
            if (types.size == 0 || types.get(0) == null)
                return null;

            for (String type : types) {
                if (type == null || !type.equals(resultType) && (!resultType.equals(neutralType) && !type.equals(neutralType)))
                    return null;
                if (!type.equals(neutralType))
                    resultType = type;
            }
        }

        return resultType;
    }
}

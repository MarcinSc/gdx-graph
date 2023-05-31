package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;

/**
 * Used for operations that accept a type and produces a result of the same type. In addition, it can accept
 * other inputs that can only be either of the same type, as the first input, or of a neutral type.
 */
public class MathCommonOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String neutralType;
    private final String[] typedInput;
    private final String[] acceptingNeutral;

    public MathCommonOutputTypeFunction(String neutralType, String[] typedInput, String[] acceptingNeutral) {
        this.neutralType = neutralType;
        this.typedInput = typedInput;
        this.acceptingNeutral = acceptingNeutral;
    }

    @Override
    public String evaluate(ObjectMap<String, Array<String>> map) {
        String resolvedType = TypeFunctions.getSameType(typedInput, map);
        if (resolvedType != null) {
            if (!TypeFunctions.checkSameType(resolvedType, acceptingNeutral, map, neutralType))
                return null;
        }
        return resolvedType;
    }
}

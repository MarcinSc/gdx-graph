package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Function;

public class MathCommonOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String floatType;
    private final String[] types;
    private final String[] floatAccepting;

    public MathCommonOutputTypeFunction(String floatType, String[] types, String[] floatAccepting) {
        this.floatType = floatType;
        this.types = types;
        this.floatAccepting = floatAccepting;
    }

    @Override
    public String apply(ObjectMap<String, Array<String>> map) {
        String resolvedType = null;
        for (String input : types) {
            Array<String> type = map.get(input);
            if (type == null || type.size == 0)
                return null;
            if (resolvedType != null && !resolvedType.equals(type.get(0)))
                return null;
            resolvedType = type.get(0);
        }

        String floatResolvedType = null;
        for (String maybeFloat : floatAccepting) {
            Array<String> type = map.get(maybeFloat);
            if (type == null || type.size == 0)
                return null;
            String type0 = type.get(0);
            if (type0 == null)
                return null;
            if (!type0.equals(floatType) && !type0.equals(resolvedType))
                return null;
            if (floatResolvedType != null && !floatResolvedType.equals(type0))
                return null;
            floatResolvedType = type0;
        }

        return resolvedType;
    }
}

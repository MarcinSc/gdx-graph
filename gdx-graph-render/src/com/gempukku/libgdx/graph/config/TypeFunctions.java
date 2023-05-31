package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class TypeFunctions {
    public static String getSameType(String[] fields, ObjectMap<String, Array<String>> types) {
        String resolvedType = null;
        for (String field : fields) {
            Array<String> inputTypes = types.get(field);
            if (inputTypes != null) {
                for (String inputType : inputTypes) {
                    if (inputType == null)
                        return null;
                    if (resolvedType != null) {
                        if (!inputType.equals(resolvedType))
                            return null;
                    } else {
                        resolvedType = inputTypes.get(0);
                    }
                }
            }
        }
        return resolvedType;
    }

    public static boolean checkSameType(String expectedType, String[] fields, ObjectMap<String, Array<String>> types, String neutralType) {
        for (String field : fields) {
            Array<String> inputTypes = types.get(field);
            if (inputTypes != null) {
                for (String type : inputTypes) {
                    if (type == null)
                        return false;
                    if (!type.equals(neutralType) && !type.equals(expectedType))
                        return false;
                }
            }
        }
        return true;
    }
}

package com.gempukku.libgdx.graph;

import java.util.LinkedHashMap;
import java.util.Map;

public class GraphTypeRegistry {
    private static Map<String, GraphType> types = new LinkedHashMap<>();

    public static GraphType findGraphType(String name) {
        return types.get(name);
    }

    public static <T extends GraphType> T findGraphType(String name, Class<T> clazz) {
        return (T) types.get(name);
    }

    public static Iterable<? extends GraphType> getAllGraphTypes() {
        return types.values();
    }

    public static void registerType(GraphType graphType) {
        if (!types.containsKey(graphType.getType())) {
            types.put(graphType.getType(), graphType);
        }
    }
}

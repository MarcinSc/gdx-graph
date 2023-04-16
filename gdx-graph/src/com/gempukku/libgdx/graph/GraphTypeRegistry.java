package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.ObjectMap;

public class GraphTypeRegistry {
    private static ObjectMap<String, GraphType> types = new ObjectMap<>();

    public static GraphType findGraphType(String name) {
        return types.get(name);
    }

    public static void registerType(GraphType graphType) {
        if (!types.containsKey(graphType.getType())) {
            types.put(graphType.getType(), graphType);
        }
    }
}

package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.ui.RenderPipelineGraphType;

public class GraphTypeRegistry {
    private static ObjectMap<String, GraphType> types = new ObjectMap<>();

    static {
        registerType(RenderPipelineGraphType.instance);
    }

    public static GraphType findGraphType(String name) {
        return types.get(name);
    }

    public static void registerType(GraphType graphType) {
        types.put(graphType.getType(), graphType);
    }
}

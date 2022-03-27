package com.gempukku.libgdx.graph.plugin.screen;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public class ScreenShaderConfiguration implements GraphConfiguration {
    public static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    public static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    static {
        // End
        addGraphShaderNodeBuilder(new EndScreenShaderNodeBuilder());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    @Override
    public Array<GraphShaderPropertyProducer> getPropertyProducers() {
        return graphShaderPropertyProducers;
    }

    @Override
    public GraphShaderNodeBuilder getGraphShaderNodeBuilder(String type) {
        return graphShaderNodeBuilders.get(type);
    }
}

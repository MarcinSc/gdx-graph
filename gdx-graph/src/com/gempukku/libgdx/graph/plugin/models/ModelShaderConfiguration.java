package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.EndShadowShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.producer.EndModelShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.provided.*;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public class ModelShaderConfiguration implements GraphConfiguration {
    public static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    public static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    static {
        // End
        addGraphShaderNodeBuilder(new EndModelShaderNodeBuilder());
        addGraphShaderNodeBuilder(new EndShadowShaderNodeBuilder());

        // Provided
        addGraphShaderNodeBuilder(new WorldPositionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ObjectToWorldShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ObjectNormalToWorldShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SkinningShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ModelFragmentCoordinateShaderNodeBuilder());
        addGraphShaderNodeBuilder(new InstanceIdShaderNodeBuilder());
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

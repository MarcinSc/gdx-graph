package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public class ParticlesShaderConfiguration implements GraphConfiguration {
    private static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    private static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    public static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    public static void addPropertyProducer(GraphShaderPropertyProducer graphShaderPropertyProducer) {
        graphShaderPropertyProducers.add(graphShaderPropertyProducer);
        ShaderFieldTypeRegistry.registerShaderFieldType(graphShaderPropertyProducer.getType());
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

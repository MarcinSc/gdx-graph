package com.gempukku.libgdx.graph.shader.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.TexturePropertyShaderCustomization;

public class PropertyShaderConfiguration implements GraphConfiguration {
    private static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    private static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    static {
        // Property
        PropertyShaderNodeBuilder propertyShaderNodeBuilder = new PropertyShaderNodeBuilder();
        propertyShaderNodeBuilder.addPropertyShaderCustomization(new TexturePropertyShaderCustomization());
        addNodeBuilder(propertyShaderNodeBuilder);
    }

    public static void addNodeBuilder(GraphShaderNodeBuilder builder) {
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
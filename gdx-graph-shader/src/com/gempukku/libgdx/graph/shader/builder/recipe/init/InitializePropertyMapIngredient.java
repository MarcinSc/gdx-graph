package com.gempukku.libgdx.graph.shader.builder.recipe.init;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public class InitializePropertyMapIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        GraphConfiguration[] configurations = GraphTypeRegistry.findGraphType(graph.getType(), ShaderGraphType.class).getConfigurations();
        int index = 0;
        for (GraphProperty property : graph.getProperties()) {
            String name = property.getName();
            PropertyLocation location = PropertyLocation.valueOf(property.getData().getString("location"));
            String attributeFunction = property.getData().getString("function", null);
            graphShader.addPropertySource(name, findPropertyProducerByType(property.getType(), configurations).createProperty(index++, name, property.getData(), location, attributeFunction, designTime, configuration));
        }
    }

    private static GraphShaderPropertyProducer findPropertyProducerByType(String type, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            for (GraphShaderPropertyProducer graphShaderPropertyProducer : configuration.getPropertyProducers()) {
                if (graphShaderPropertyProducer.getType().getName().equals(type))
                    return graphShaderPropertyProducer;
            }
        }

        return null;
    }
}

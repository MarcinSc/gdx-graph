package com.gempukku.libgdx.graph.shader.builder.recipe;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public interface GraphShaderRecipeIngredient {
    void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration);

    interface GraphShaderOutputResolver {
        GraphShaderNodeBuilder.FieldOutput getSingleOutput(String nodeId, String property);
        GraphShaderNodeBuilder.FieldOutput getSingleOutputForInput(String inputNodeId, String inputProperty);
    }
}

package com.gempukku.libgdx.graph.shader.builder.recipe.init;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class InitializeDepthShaderIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        graphShader.setDepthWriting(true);
    }
}

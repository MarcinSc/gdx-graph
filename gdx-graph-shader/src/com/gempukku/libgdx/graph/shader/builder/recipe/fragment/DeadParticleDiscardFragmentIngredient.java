package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class DeadParticleDiscardFragmentIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        // Fragment part
        fragmentShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");
        fragmentShaderBuilder.addMainLine("if (u_time >= 0.0)");
        fragmentShaderBuilder.addMainLine("  discard;");
    }
}

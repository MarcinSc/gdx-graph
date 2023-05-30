package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class FillColorFragmentIngredient implements GraphShaderRecipeIngredient {
    private final Color color;

    public FillColorFragmentIngredient(Color color) {
        this.color = color;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = vec4(" + SimpleNumberFormatter.format(color.r)+", "+SimpleNumberFormatter.format(color.g)+", "+SimpleNumberFormatter.format(color.b)+", "+SimpleNumberFormatter.format(color.a)+");");
    }
}

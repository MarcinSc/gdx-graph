package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class DepthFragmentIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        fragmentShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping,
                "Near/far clipping");
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");
        fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");

        if (!fragmentShaderBuilder.containsFunction("packFloatToVec3")) {
            fragmentShaderBuilder.addFunction("packFloatToVec3", GLSLFragmentReader.getFragment(configuration.getAssetResolver(), "packFloatToVec3"));
        }
        fragmentShaderBuilder.addMainLine("gl_FragColor = vec4(packFloatToVec3(distance(v_position_world, u_cameraPosition), u_cameraClipping.x, u_cameraClipping.y), 1.0);");
    }
}

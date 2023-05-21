package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class DeadParticleDiscardFragmentIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        // Fragment part
        if (!vertexShaderBuilder.hasVaryingVariable("v_deathTime")) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(2048, 1, "a_deathTime"), "float", "Particle death-time");
            vertexShaderBuilder.addVaryingVariable("v_deathTime", "float");
            vertexShaderBuilder.addMainLine("v_deathTime = a_deathTime;");

            fragmentShaderBuilder.addVaryingVariable("v_deathTime", "float");
        }

        fragmentShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time,
                "Time");
        fragmentShaderBuilder.addMainLine("if (u_time >= v_deathTime)");
        fragmentShaderBuilder.addMainLine("  discard;");
    }
}

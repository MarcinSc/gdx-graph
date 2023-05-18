package com.gempukku.libgdx.graph.shader.builder.recipe.init;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class SetupFloatPrevisionIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        vertexShaderBuilder.addInitialLine("#ifdef GL_ES");
        vertexShaderBuilder.addInitialLine("#define LOWP lowp");
        vertexShaderBuilder.addInitialLine("#define MED mediump");
        vertexShaderBuilder.addInitialLine("#define HIGH highp");
        vertexShaderBuilder.addInitialLine("precision mediump float;");
        vertexShaderBuilder.addInitialLine("#else");
        vertexShaderBuilder.addInitialLine("#define MED");
        vertexShaderBuilder.addInitialLine("#define LOWP");
        vertexShaderBuilder.addInitialLine("#define HIGH");
        vertexShaderBuilder.addInitialLine("#endif");

        fragmentShaderBuilder.addInitialLine("#ifdef GL_ES");
        fragmentShaderBuilder.addInitialLine("#define LOWP lowp");
        fragmentShaderBuilder.addInitialLine("#define MED mediump");
        fragmentShaderBuilder.addInitialLine("#define HIGH highp");
        fragmentShaderBuilder.addInitialLine("precision mediump float;");
        fragmentShaderBuilder.addInitialLine("#else");
        fragmentShaderBuilder.addInitialLine("#define MED");
        fragmentShaderBuilder.addInitialLine("#define LOWP");
        fragmentShaderBuilder.addInitialLine("#define HIGH");
        fragmentShaderBuilder.addInitialLine("#endif");
    }
}

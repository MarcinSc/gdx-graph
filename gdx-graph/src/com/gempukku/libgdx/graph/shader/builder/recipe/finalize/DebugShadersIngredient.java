package com.gempukku.libgdx.graph.shader.builder.recipe.finalize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class DebugShadersIngredient implements GraphShaderRecipeIngredient {
    private final String type;

    public DebugShadersIngredient(String type) {
        this.type = type;
    }

    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "Vertex " + type + " shader:");
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "\n" + vertexShader);
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "Fragment " + type + " shader:");
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "\n" + fragmentShader);
    }
}

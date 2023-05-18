package com.gempukku.libgdx.graph.shader.builder.recipe.vertex;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;

public class ScreenPositionVertexShaderIngredient implements GraphShaderRecipeIngredient {
    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), "vec3", "Position");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = vec4((a_position.xy * 2.0 - 1.0), 1.0, 1.0);");
    }
}

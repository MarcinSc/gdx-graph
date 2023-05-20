package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.FieldOutputSource;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class AlphaDiscardFragmentIngredient implements GraphShaderRecipeIngredient {
    private final FieldOutputSource alphaSource;
    private final FieldOutputSource alphaClipSource;

    public AlphaDiscardFragmentIngredient(FieldOutputSource alphaSource, FieldOutputSource alphaClipSource) {
        this.alphaSource = alphaSource;
        this.alphaClipSource = alphaClipSource;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        GraphShaderNodeBuilder.FieldOutput alphaField = alphaSource.resolveOutput(outputResolver);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = alphaClipSource.resolveOutput(outputResolver);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";

        if (alphaField != null || alphaClipField != null) {
            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }
}

package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class AlphaDiscardFragmentIngredient implements GraphShaderRecipeIngredient {
    private String alphaNodeId;
    private String alphaProperty;
    private String alphaClipNodeId;
    private String alphaClipProperty;

    public AlphaDiscardFragmentIngredient(String alphaNodeId, String alphaProperty, String alphaClipNodeId, String alphaClipProperty) {
        this.alphaNodeId = alphaNodeId;
        this.alphaProperty = alphaProperty;
        this.alphaClipNodeId = alphaClipNodeId;
        this.alphaClipProperty = alphaClipProperty;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        GraphShaderNodeBuilder.FieldOutput alphaField = outputResolver.getSingleOutputForInput(alphaNodeId, alphaProperty);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = outputResolver.getSingleOutputForInput(alphaClipNodeId, alphaClipProperty);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";

        if (alphaField != null || alphaClipField != null) {
            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }
}

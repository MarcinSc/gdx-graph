package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.FieldOutputSource;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class DiscardFragmentIngredient implements GraphShaderRecipeIngredient {
    private final FieldOutputSource discardValueSource;

    public DiscardFragmentIngredient(FieldOutputSource discardValueSource) {
        this.discardValueSource = discardValueSource;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        GraphShaderNodeBuilder.FieldOutput discardValueField = discardValueSource.resolveOutput(outputResolver);
        if (discardValueField != null) {
            String discardValue = discardValueField.getRepresentation();
            String discardComparison = discardValueSource.resolveNode(graph).getData().getString("discardComparison", "<=");
            float discardValueToCompareTo = discardValueSource.resolveNode(graph).getData().getFloat("discardValue", 0f);

            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + discardValue + " "+discardComparison+" " + SimpleNumberFormatter.format(discardValueToCompareTo) + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }
}

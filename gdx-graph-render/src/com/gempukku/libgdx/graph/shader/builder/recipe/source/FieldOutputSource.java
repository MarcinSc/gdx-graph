package com.gempukku.libgdx.graph.shader.builder.recipe.source;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.GraphNode;

public interface FieldOutputSource {
    GraphShaderNodeBuilder.FieldOutput resolveOutput(GraphShaderRecipeIngredient.GraphShaderOutputResolver outputResolver);

    GraphNode resolveNode(GraphWithProperties graphWithProperties);
}

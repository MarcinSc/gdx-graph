package com.gempukku.libgdx.graph.shader.builder.recipe.source;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.ui.graph.data.GraphNode;

public class InputSource implements FieldOutputSource {
    private final String nodeId;
    private final String property;

    public InputSource(String nodeId, String property) {
        this.nodeId = nodeId;
        this.property = property;
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput resolveOutput(GraphShaderRecipeIngredient.GraphShaderOutputResolver outputResolver) {
        return outputResolver.getSingleOutputForInput(nodeId, property);
    }

    @Override
    public GraphNode resolveNode(GraphWithProperties graphWithProperties) {
        return graphWithProperties.getNodeById(nodeId);
    }
}

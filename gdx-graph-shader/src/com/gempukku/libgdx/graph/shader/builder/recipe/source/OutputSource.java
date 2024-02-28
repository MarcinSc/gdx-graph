package com.gempukku.libgdx.graph.shader.builder.recipe.source;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.data.GraphNode;

public class OutputSource implements FieldOutputSource {
    private final String nodeId;
    private final String property;

    public OutputSource(String nodeId, String property) {
        this.nodeId = nodeId;
        this.property = property;
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput resolveOutput(GraphShaderRecipeIngredient.GraphShaderOutputResolver outputResolver) {
        return outputResolver.getSingleOutput(nodeId, property);
    }

    @Override
    public GraphNode resolveNode(GraphWithProperties graphWithProperties) {
        return graphWithProperties.getNodeById(nodeId);
    }
}

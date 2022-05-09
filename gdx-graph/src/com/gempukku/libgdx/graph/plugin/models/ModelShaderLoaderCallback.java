package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.*;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class ModelShaderLoaderCallback extends GraphDataLoaderCallback<GraphShader, ShaderFieldType> {
    private final String tag;
    private final Texture defaultTexture;
    private final boolean depthShader;
    private final GraphConfiguration[] graphConfigurations;

    public ModelShaderLoaderCallback(String tag, Texture defaultTexture,
                                     boolean depthShader, GraphConfiguration... graphConfiguration) {
        this.tag = tag;
        this.defaultTexture = defaultTexture;
        this.depthShader = depthShader;
        graphConfigurations = graphConfiguration;
    }

    @Override
    public void start() {

    }

    @Override
    public GraphShader end() {
        GraphValidator<GraphNode, GraphConnection, GraphProperty> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode, GraphConnection, GraphProperty> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        if (depthShader)
            return GraphShaderBuilder.buildModelDepthShader(tag, defaultTexture, this, false);
        else
            return GraphShaderBuilder.buildModelShader(tag, defaultTexture, this, false);
    }

    @Override
    protected NodeConfiguration getNodeConfiguration(String type, JsonValue data) {
        for (GraphConfiguration graphConfiguration : graphConfigurations) {
            GraphShaderNodeBuilder graphShaderNodeBuilder = graphConfiguration.getGraphShaderNodeBuilder(type);
            if (graphShaderNodeBuilder != null)
                return graphShaderNodeBuilder.getConfiguration(data);
        }

        return null;
    }
}

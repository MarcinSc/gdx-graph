package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;

public class ModelShaderLoader {
    public static GraphShader loadShader(JsonValue jsonGraph, String tag, boolean depthShader, Texture defaultTexture) {
        GraphType graphType = GraphTypeRegistry.findGraphType("Model_Shader");

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), jsonGraph);

        if (graphType.getGraphValidator().validateGraph(graph).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        if (depthShader)
            return GraphShaderBuilder.buildModelDepthShader(tag, defaultTexture, graph, false);
        else
            return GraphShaderBuilder.buildModelShader(tag, defaultTexture, graph, false);
    }
}

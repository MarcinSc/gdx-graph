package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;

public class ModelShaderLoader {
    public static GraphShader loadShader(JsonValue jsonGraph, String tag, boolean depthShader) {
        return loadShader(jsonGraph, tag, depthShader, new InternalFileHandleResolver());
    }

    public static GraphShader loadShader(JsonValue jsonGraph, String tag, boolean depthShader, FileHandleResolver assetResolver) {
        GraphType graphType = GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE);

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), jsonGraph);

        if (graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation()).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        if (depthShader)
            return GraphShaderBuilder.buildModelDepthShader(tag, assetResolver, graph, false);
        else
            return GraphShaderBuilder.buildModelShader(tag, assetResolver, graph, false);
    }
}

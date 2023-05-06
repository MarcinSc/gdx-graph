package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;

public class ShadowShaderLoader {
    public static GraphShader loadShader(JsonValue jsonGraph, String tag, Texture defaultTexture) {
        return loadShader(jsonGraph, tag, defaultTexture, new InternalFileHandleResolver());
    }

    public static GraphShader loadShader(JsonValue jsonGraph, String tag, Texture defaultTexture, FileHandleResolver assetResolver) {
        GraphType graphType = GraphTypeRegistry.findGraphType(ShadowShaderGraphType.TYPE);

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), jsonGraph);

        if (graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation()).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        return GraphShaderBuilder.buildModelDepthShader(tag, defaultTexture, assetResolver, graph, false);
    }
}

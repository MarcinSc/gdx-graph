package com.gempukku.libgdx.graph.shader.depth;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderGraphType;

public class DepthShaderLoader {
    public static GraphShader loadShader(FileHandle graphFile, String graphTypeOverride, String tag, PipelineRendererConfiguration configuration) {
        ShaderGraphType graphType = GraphTypeRegistry.findGraphType(DepthShaderGraphType.TYPE, ShaderGraphType.class);

        GraphWithProperties graph = GraphLoader.loadGraph(graphTypeOverride, new JsonReader().parse(graphFile));

        if (graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation()).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        return graphType.buildGraphShader(tag, configuration, graph, false);
    }

    public static GraphShader loadShader(FileHandle graphFile, String tag, PipelineRendererConfiguration configuration) {
        ShaderGraphType graphType = GraphTypeRegistry.findGraphType(DepthShaderGraphType.TYPE, ShaderGraphType.class);
        return loadShader(graphFile, graphType.getType(), tag, configuration);
    }
}

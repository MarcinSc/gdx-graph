package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;

public class ModelShaderLoader {
    public static GraphShader loadShader(FileHandle fileHandle, String tag, PipelineRendererConfiguration configuration) {
        ShaderGraphType graphType = GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE, ShaderGraphType.class);

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), new JsonReader().parse(fileHandle));

        if (graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation()).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        return graphType.buildGraphShader(tag, configuration, graph, false);
    }
}

package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultPipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.impl.GraphPreparedRenderingPipeline;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;

import java.io.IOException;
import java.io.InputStream;

public class PipelineLoader {
    public static PipelineRenderer loadPipelineRenderer(FileHandle pipelineFile, PipelineRendererConfiguration configuration) {
        try (InputStream inputStream = pipelineFile.read()) {
            return loadPipelineRenderer(inputStream, configuration);
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }

    public static PipelineRenderer loadPipelineRenderer(InputStream pipelineInputStream, PipelineRendererConfiguration configuration) {
        try {
            RuntimePluginRegistry pluginRegistry = RuntimePluginRegistry.initializePlugins();

            GraphType graphType = GraphTypeRegistry.findGraphType(RenderPipelineGraphType.TYPE);

            GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), pipelineInputStream);

            GraphValidationResult validationResult = graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation());
            if (validationResult.hasErrors())
                throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

            GraphPipelinePropertySource pipelinePropertySource = new GraphPipelinePropertySource(graph);
            configuration.initialize(pipelinePropertySource);

            GraphPreparedRenderingPipeline renderingPipeline = new GraphPreparedRenderingPipeline(graph, configuration,"end");

            return new DefaultPipelineRenderer(
                    renderingPipeline, configuration);
        } catch (ReflectionException exp) {
            throw new GdxRuntimeException("Unable to initialize plugins", exp);
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }
}

package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultPipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.impl.PreparedRenderingPipelineImpl;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;

import java.io.IOException;
import java.io.InputStream;

public class PipelineLoader {
    public static PipelineRenderer loadPipelineRenderer(FileHandle pipelineFile, TimeProvider timeProvider) {
        return loadPipelineRenderer(pipelineFile, timeProvider, null);
    }

    public static PipelineRenderer loadPipelineRenderer(FileHandle pipelineFile, TimeProvider timeProvider, PipelineRendererResources resources) {
        try (InputStream inputStream = pipelineFile.read()) {
            return loadPipelineRenderer(inputStream, timeProvider, resources);
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }

    public static PipelineRenderer loadPipelineRenderer(InputStream pipelineInputStream, TimeProvider timeProvider) {
        return loadPipelineRenderer(pipelineInputStream, timeProvider, null);
    }

    public static PipelineRenderer loadPipelineRenderer(InputStream pipelineInputStream, TimeProvider timeProvider, PipelineRendererResources resources) {
        try {
            RuntimePluginRegistry pluginRegistry = RuntimePluginRegistry.initializePlugins();

            GraphType graphType = GraphTypeRegistry.findGraphType(RenderPipelineGraphType.TYPE);

            GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), pipelineInputStream);

            GraphValidationResult validationResult = graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation());
            if (validationResult.hasErrors())
                throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

            ObjectMap<String, PipelineProperty> propertyMap = new ObjectMap<>();
            for (GraphProperty property : graph.getProperties()) {
                PipelinePropertyProducer propertyProducer = RendererPipelineConfiguration.findPropertyProducer(property.getType());
                if (propertyProducer == null)
                    throw new GdxRuntimeException("Unknown type of property: " + property.getType());
                propertyMap.put(property.getName(), propertyProducer.createProperty(property.getData()));
            }

            return new DefaultPipelineRenderer(pluginRegistry, timeProvider,
                    new PreparedRenderingPipelineImpl(graph, "end"), propertyMap, resources);
        } catch (ReflectionException exp) {
            throw new GdxRuntimeException("Unable to initialize plugins", exp);
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }
}

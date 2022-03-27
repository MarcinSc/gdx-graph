package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.data.*;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.PipelineRendererImpl;
import com.gempukku.libgdx.graph.pipeline.impl.PreparedRenderingPipelineImpl;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class PipelineLoaderCallback extends GraphDataLoaderCallback<PipelineRenderer, PipelineFieldType> {
    private final TimeProvider timeProvider;
    private final PipelineRendererResources resources;
    private final String outputNodeId;
    private PluginRegistryImpl pluginRegistry;

    public PipelineLoaderCallback(TimeProvider timeProvider) {
        this(timeProvider, null);
    }

    public PipelineLoaderCallback(TimeProvider timeProvider, PipelineRendererResources resources) {
        this(timeProvider, resources, "end");
    }

    public PipelineLoaderCallback(TimeProvider timeProvider, PipelineRendererResources resources, String outputNodeId) {
        this.timeProvider = timeProvider;
        this.resources = resources;
        this.outputNodeId = outputNodeId;
    }

    @Override
    public void start() {
        try {
            pluginRegistry = PluginRegistryImpl.initializePlugins();
        } catch (ReflectionException e) {
            throw new GdxRuntimeException(e);
        }
    }

    @Override
    public PipelineRenderer end() {
        GraphValidator<GraphNode, GraphConnection, GraphProperty> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode, GraphConnection, GraphProperty> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        ObjectMap<String, WritablePipelineProperty> propertyMap = new ObjectMap<>();
        for (GraphProperty property : getProperties()) {
            propertyMap.put(property.getName(), RendererPipelineConfiguration.findPropertyProducer(property.getType()).createProperty(property.getData()));
        }

        return new PipelineRendererImpl(pluginRegistry, timeProvider,
                new PreparedRenderingPipelineImpl(this, outputNodeId), propertyMap, resources);
    }

    @Override
    protected NodeConfiguration getNodeConfiguration(String type, JsonValue data) {
        PipelineNodeProducer producer = RendererPipelineConfiguration.findProducer(type);
        if (producer == null)
            throw new IllegalArgumentException("Unknown type of producer: " + type);
        return producer.getConfiguration(data);
    }
}

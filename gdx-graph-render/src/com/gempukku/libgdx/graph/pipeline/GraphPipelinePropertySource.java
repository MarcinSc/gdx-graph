package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;

public class GraphPipelinePropertySource implements PipelinePropertySource {
    private ObjectMap<String, PipelineProperty> pipelineProperties = new ObjectMap<>();

    public GraphPipelinePropertySource(GraphWithProperties graph) {
        for (final GraphProperty property : graph.getProperties()) {
            PipelinePropertyProducer propertyProducer = RendererPipelineConfiguration.findPropertyProducer(property.getType());
            if (propertyProducer == null)
                throw new GdxRuntimeException("Unknown type of property: " + property.getType());
            pipelineProperties.put(property.getName(), propertyProducer.createProperty(property.getData()));
        }
    }

    @Override
    public PipelineProperty getPipelineProperty(String property) {
        return pipelineProperties.get(property);
    }

    @Override
    public boolean hasPipelineProperty(String property) {
        return pipelineProperties.containsKey(property);
    }

    @Override
    public Iterable<? extends PipelineProperty> getProperties() {
        return pipelineProperties.values();
    }
}

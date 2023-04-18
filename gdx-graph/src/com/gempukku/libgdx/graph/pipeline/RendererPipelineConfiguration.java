package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;

public class RendererPipelineConfiguration {
    private static final ObjectMap<String, PipelineNodeProducer> pipelineNodeProducers = new ObjectMap<>();
    private static final Array<PipelinePropertyProducer> pipelinePropertyProducers = new Array<>();

    public static void register(PipelineNodeProducer pipelineNodeProducer) {
        pipelineNodeProducers.put(pipelineNodeProducer.getType(), pipelineNodeProducer);
    }

    public static void registerPropertyProducer(PipelinePropertyProducer pipelinePropertyProducer, PipelineFieldType pipelineFieldType) {
        pipelinePropertyProducers.add(pipelinePropertyProducer);
        PipelineFieldTypeRegistry.registerPipelineFieldType(pipelineFieldType);
    }

    public static PipelineNodeProducer findProducer(String type) {
        return pipelineNodeProducers.get(type);
    }

    public static PipelineFieldType findFieldType(String type) {
        return PipelineFieldTypeRegistry.findPipelineFieldType(type);
    }

    public static PipelinePropertyProducer findPropertyProducer(String type) {
        for (PipelinePropertyProducer pipelinePropertyProducer : pipelinePropertyProducers) {
            if (pipelinePropertyProducer.getType().equals(type))
                return pipelinePropertyProducer;
        }
        return null;
    }

    private RendererPipelineConfiguration() {

    }
}

package com.gempukku.libgdx.graph.render.maps;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsLayerIdsRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsLayersRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsRendererPipelineNodeProducer;

public class MapsPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        RendererPipelineConfiguration.register(new MapsRendererPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MapsLayersRendererPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MapsLayerIdsRendererPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}

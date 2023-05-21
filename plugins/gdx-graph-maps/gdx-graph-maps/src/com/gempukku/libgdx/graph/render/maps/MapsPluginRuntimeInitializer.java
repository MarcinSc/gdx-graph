package com.gempukku.libgdx.graph.render.maps;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsLayerIdsRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsLayersRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.maps.producer.MapsRendererPipelineNodeProducer;

public class MapsPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private MapsPluginPrivateData data;

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        data = new MapsPluginPrivateData();

        RendererPipelineConfiguration.register(new MapsRendererPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MapsLayersRendererPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MapsLayerIdsRendererPipelineNodeProducer());

        pluginRegistry.registerPrivateData(MapsPluginPrivateData.class, data);
        pluginRegistry.registerPublicData(MapsPluginData.class, data);
    }

    @Override
    public void dispose() {

    }
}

package com.gempukku.libgdx.graph.render.callback;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.callback.producer.RenderCallbackPipelineNodeProducer;

public class RenderCallbackPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RenderCallbackPrivateData data = new RenderCallbackPrivateData();

        RendererPipelineConfiguration.register(new RenderCallbackPipelineNodeProducer());

        pluginRegistry.registerPrivateData(RenderCallbackPrivateData.class, data);
        pluginRegistry.registerPublicData(RenderCallbackPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}

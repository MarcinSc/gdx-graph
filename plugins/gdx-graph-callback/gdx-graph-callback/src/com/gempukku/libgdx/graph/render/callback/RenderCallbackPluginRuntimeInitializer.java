package com.gempukku.libgdx.graph.render.callback;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.callback.producer.RenderCallbackPipelineNodeProducer;

public class RenderCallbackPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        RendererPipelineConfiguration.register(new RenderCallbackPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}

package com.gempukku.libgdx.graph.render.ui;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.ui.producer.UIRendererPipelineNodeProducer;

public class UIPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        RendererPipelineConfiguration.register(new UIRendererPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}

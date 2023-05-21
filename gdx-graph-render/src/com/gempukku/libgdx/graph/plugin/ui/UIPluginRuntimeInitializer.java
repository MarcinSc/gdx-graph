package com.gempukku.libgdx.graph.plugin.ui;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class UIPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private final UIPluginPrivateData data = new UIPluginPrivateData();

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new UIRendererPipelineNodeProducer());

        pluginRegistry.registerPrivateData(UIPluginPrivateData.class, data);
        pluginRegistry.registerPublicData(UIPluginPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}

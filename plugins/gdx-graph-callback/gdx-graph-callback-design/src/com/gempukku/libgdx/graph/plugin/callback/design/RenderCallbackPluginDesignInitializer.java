package com.gempukku.libgdx.graph.plugin.callback.design;


import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.RenderCallbackBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class RenderCallbackPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        RenderCallbackPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new RenderCallbackBoxProducer());
    }
}

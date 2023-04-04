package com.gempukku.libgdx.graph.plugin.callback.design;

import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.RenderCallbackBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;

public class RenderCallbackPlugin {
    public void initialize() {
        RenderCallbackPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new RenderCallbackBoxProducer());
    }
}

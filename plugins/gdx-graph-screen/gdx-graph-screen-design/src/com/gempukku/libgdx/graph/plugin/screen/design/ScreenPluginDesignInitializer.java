package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.ScreenShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class ScreenPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        ScreenPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ScreenShaderRendererBoxProducer());
    }
}

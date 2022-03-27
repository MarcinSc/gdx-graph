package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class ModelsPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        ModelsPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ModelShaderRendererBoxProducer());
    }
}

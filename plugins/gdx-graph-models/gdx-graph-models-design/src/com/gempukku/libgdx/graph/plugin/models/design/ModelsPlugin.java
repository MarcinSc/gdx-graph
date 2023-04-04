package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;

public class ModelsPlugin {
    public void initialize() {
        ModelsPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ModelShaderRendererBoxProducer());
    }
}

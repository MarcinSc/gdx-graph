package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticlesShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;

public class ParticlesPlugin {
    public void initialize() {
        ParticlesPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ParticlesShaderRendererBoxProducer());
    }
}

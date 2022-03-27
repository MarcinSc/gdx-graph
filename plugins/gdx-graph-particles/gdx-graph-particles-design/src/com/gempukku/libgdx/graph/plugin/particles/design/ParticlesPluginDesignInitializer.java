package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticlesShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class ParticlesPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        ParticlesPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ParticlesShaderRendererBoxProducer());
    }
}

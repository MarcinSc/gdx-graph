package com.gempukku.libgdx.graph.plugin.particles;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class ParticlesPluginRuntimeInitializer implements PluginRuntimeInitializer {
    public static void register() {
        GraphTypeRegistry.registerType(new ParticleEffectGraphType());

        PluginRegistryImpl.register(ParticlesPluginRuntimeInitializer.class);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new ParticlesShaderRendererPipelineNodeProducer(pluginRegistry));
    }

    @Override
    public void dispose() {

    }
}

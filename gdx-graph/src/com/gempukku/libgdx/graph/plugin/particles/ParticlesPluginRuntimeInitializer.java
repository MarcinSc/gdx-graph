package com.gempukku.libgdx.graph.plugin.particles;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.impl.GraphParticleEffectsImpl;

public class ParticlesPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private final GraphParticleEffectsImpl effects = new GraphParticleEffectsImpl();

    public static void register() {
        PluginRegistryImpl.register(ParticlesPluginRuntimeInitializer.class);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new ParticlesShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphParticleEffectsImpl.class, effects);
        pluginRegistry.registerPublicData(GraphParticleEffects.class, effects);
    }

    @Override
    public void dispose() {
        effects.dispose();
    }
}

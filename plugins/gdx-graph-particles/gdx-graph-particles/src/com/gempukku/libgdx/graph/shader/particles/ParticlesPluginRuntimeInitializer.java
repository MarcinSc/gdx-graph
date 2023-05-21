package com.gempukku.libgdx.graph.shader.particles;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.particles.particle.EndParticlesShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.particles.particle.ParticleLifePercentageShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.particles.particle.ParticleLifetimeShaderNodeBuilder;

public class ParticlesPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphTypeRegistry.registerType(new ParticleEffectGraphType());

        // End
        ParticlesShaderConfiguration.addGraphShaderNodeBuilder(new EndParticlesShaderNodeBuilder());

        // Particle
        ParticlesShaderConfiguration.addGraphShaderNodeBuilder(new ParticleLifetimeShaderNodeBuilder());
        ParticlesShaderConfiguration.addGraphShaderNodeBuilder(new ParticleLifePercentageShaderNodeBuilder());

        RendererPipelineConfiguration.register(new ParticlesShaderRendererPipelineNodeProducer(pluginRegistry));
    }

    @Override
    public void dispose() {

    }
}

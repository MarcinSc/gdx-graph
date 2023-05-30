package com.gempukku.libgdx.graph.shader.particles;

import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.particles.particle.ParticleLifePercentageShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.particles.particle.ParticleLifetimeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.particles.particle.ParticleTimeToDeathShaderNodeBuilder;

public class ParticlesPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        ModelShaderConfiguration.addNodeBuilder(new ParticleLifetimeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ParticleLifePercentageShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ParticleTimeToDeathShaderNodeBuilder());
    }

    @Override
    public void dispose() {

    }
}

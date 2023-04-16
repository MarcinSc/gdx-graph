package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ParticleLifetimeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ParticleLifetimeShaderNodeConfiguration() {
        super("ParticleLifetime", "Particle lifetime", "Particle");
        addNodeOutput(
                new DefaultGraphNodeOutput("time", "Time", ShaderFieldType.Float));
    }
}

package com.gempukku.libgdx.graph.shader.particles.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ParticleLifetimeShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ParticleLifetimeShaderNodeConfiguration() {
        super("NewParticleLifetime", "Particle lifetime", "Particle");
        addNodeInput(
                new DefaultGraphNodeInput("birth", "Birth", ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("death", "Death", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("time", "Time", ShaderFieldType.Float));
    }
}

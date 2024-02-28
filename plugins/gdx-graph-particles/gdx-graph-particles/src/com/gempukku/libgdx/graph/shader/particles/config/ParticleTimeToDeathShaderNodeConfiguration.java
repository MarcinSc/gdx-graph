package com.gempukku.libgdx.graph.shader.particles.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ParticleTimeToDeathShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ParticleTimeToDeathShaderNodeConfiguration() {
        super("ParticleTimeToDeath", "Particle time to death", "Particle");
        addNodeInput(
                new DefaultGraphNodeInput("death", "Death", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("time", "Time", ShaderFieldType.Float));
    }
}

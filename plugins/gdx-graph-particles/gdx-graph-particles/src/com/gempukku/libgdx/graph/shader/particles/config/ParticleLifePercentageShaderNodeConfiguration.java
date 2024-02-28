package com.gempukku.libgdx.graph.shader.particles.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ParticleLifePercentageShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ParticleLifePercentageShaderNodeConfiguration() {
        super("NewParticleLifePercentage", "Particle life %", "Particle");
        addNodeInput(
                new DefaultGraphNodeInput("birth", "Birth", ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("death", "Death", ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("percentage", "Percentage", ShaderFieldType.Float));
    }
}

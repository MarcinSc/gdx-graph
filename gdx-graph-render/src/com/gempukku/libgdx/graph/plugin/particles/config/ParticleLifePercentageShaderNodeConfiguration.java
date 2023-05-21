package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class ParticleLifePercentageShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ParticleLifePercentageShaderNodeConfiguration() {
        super("ParticleLifePercentage", "Particle life %", "Particle");
        addNodeOutput(
                new DefaultGraphNodeOutput("percentage", "Percentage", ShaderFieldType.Float));
    }
}

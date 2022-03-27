package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticleLifePercentageShaderNodeConfiguration extends NodeConfigurationImpl {
    public ParticleLifePercentageShaderNodeConfiguration() {
        super("ParticleLifePercentage", "Particle life %", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl("percentage", "Percentage", ShaderFieldType.Float));
    }
}

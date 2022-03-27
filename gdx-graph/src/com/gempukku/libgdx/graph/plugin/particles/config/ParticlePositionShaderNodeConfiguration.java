package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticlePositionShaderNodeConfiguration extends NodeConfigurationImpl {
    public ParticlePositionShaderNodeConfiguration() {
        super("ParticlePosition", "Particle position", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl("position", "Position", ShaderFieldType.Vector3));
    }
}

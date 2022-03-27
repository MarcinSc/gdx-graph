package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticleUVShaderNodeConfiguration extends NodeConfigurationImpl {
    public ParticleUVShaderNodeConfiguration() {
        super("ParticleUV", "Particle UV", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl("uv", "UV", ShaderFieldType.Vector2));
    }
}

package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;

public interface PreviewParticleGeneratorProducer {
    DefaultParticleGenerator createGenerator(float lifeLength, int initialParticles, float particlesPerSecond, ObjectMap<String, ShaderPropertySource> shaderPropertySources);
}

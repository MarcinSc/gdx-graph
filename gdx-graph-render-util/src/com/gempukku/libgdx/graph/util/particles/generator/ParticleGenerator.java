package com.gempukku.libgdx.graph.util.particles.generator;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface ParticleGenerator {
    void initialCreateParticles(float currentTime, ParticleCreateCallback createCallback);

    void createParticles(float currentTime, ParticleCreateCallback createCallback);

    interface ParticleCreateCallback {
        void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer);
    }
}

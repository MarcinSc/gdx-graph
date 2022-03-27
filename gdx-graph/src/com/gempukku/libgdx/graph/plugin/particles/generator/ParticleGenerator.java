package com.gempukku.libgdx.graph.plugin.particles.generator;

import com.badlogic.gdx.utils.ObjectMap;

public interface ParticleGenerator<T> {
    void initialCreateParticles(ParticleCreateCallback<T> createCallback);

    void createParticles(ParticleCreateCallback<T> createCallback);

    interface ParticleCreateCallback<T> {
        void createParticle(float particleBirth, float lifeLength, ObjectMap<String, Object> attributes);

        void createParticle(float particleBirth, float lifeLength, T particleData, ObjectMap<String, Object> attributes);
    }
}

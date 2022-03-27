package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.utils.ObjectMap;

public interface ParticleUpdater<T> {
    void updateParticle(T particleData, ParticleUpdateCallback callback);

    interface ParticleUpdateCallback {
        void updateParticle(ObjectMap<String, Object> attributes);
    }
}

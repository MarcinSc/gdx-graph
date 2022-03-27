package com.gempukku.libgdx.graph.plugin.particles;

public interface GraphParticleEffect {
    boolean isRunning();

    String getTag();

    RenderableParticleEffect getRenderableParticleEffect();
}

package com.gempukku.libgdx.graph.artemis.particle;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.util.particles.generator.value.FloatValue;

public class ParticleEffect {
    private String particleBatchName;

    private FloatValue variableLifeLength;
    private float lifeLength;
    private FloatValue variableInitialParticles;
    private float initialParticles;
    private FloatValue variableParticlesPerSecond;
    private float particlesPerSecond;

    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public String getParticleBatchName() {
        return particleBatchName;
    }

    public void setParticleBatchName(String particleBatchName) {
        this.particleBatchName = particleBatchName;
    }

    public FloatValue getVariableLifeLength() {
        return variableLifeLength;
    }

    public void setVariableLifeLength(FloatValue variableLifeLength) {
        this.variableLifeLength = variableLifeLength;
    }

    public float getLifeLength() {
        return lifeLength;
    }

    public void setLifeLength(float lifeLength) {
        this.lifeLength = lifeLength;
    }

    public FloatValue getVariableInitialParticles() {
        return variableInitialParticles;
    }

    public void setVariableInitialParticles(FloatValue variableInitialParticles) {
        this.variableInitialParticles = variableInitialParticles;
    }

    public float getInitialParticles() {
        return initialParticles;
    }

    public void setInitialParticles(float initialParticles) {
        this.initialParticles = initialParticles;
    }

    public FloatValue getVariableParticlesPerSecond() {
        return variableParticlesPerSecond;
    }

    public void setVariableParticlesPerSecond(FloatValue variableParticlesPerSecond) {
        this.variableParticlesPerSecond = variableParticlesPerSecond;
    }

    public float getParticlesPerSecond() {
        return particlesPerSecond;
    }

    public void setParticlesPerSecond(float particlesPerSecond) {
        this.particlesPerSecond = particlesPerSecond;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}

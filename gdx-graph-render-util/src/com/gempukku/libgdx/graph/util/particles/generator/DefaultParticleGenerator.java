package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.util.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.util.particles.generator.value.StaticFloatValue;

public class DefaultParticleGenerator implements ParticleGenerator {
    private FloatValue lifeLength;
    private FloatValue initialParticles;
    private FloatValue particlesPerSecond;

    private MapWritablePropertyContainer tempPropertyContainer = new MapWritablePropertyContainer();

    private ObjectMap<String, PropertyGenerator> propertyGenerators = new ObjectMap<>();

    private float lastParticleGenerated;

    public DefaultParticleGenerator(float lifeLength,
                                    int initialParticles, float particlesPerSecond) {
        this(new StaticFloatValue(lifeLength), new StaticFloatValue(initialParticles), new StaticFloatValue(particlesPerSecond));
    }

    public DefaultParticleGenerator(float minLifeLength, float maxLifeLength,
                                    int initialParticles, float particlesPerSecond) {
        this(new RangeFloatValue(minLifeLength, maxLifeLength), new StaticFloatValue(initialParticles),
                new StaticFloatValue(particlesPerSecond));
    }

    public DefaultParticleGenerator(FloatValue lifeLength,
                                    FloatValue initialParticles, FloatValue particlesPerSecond) {
        this.lifeLength = lifeLength;
        this.initialParticles = initialParticles;
        this.particlesPerSecond = particlesPerSecond;
    }

    public void setLifeLength(float lifeLength) {
        setLifeLength(new StaticFloatValue(lifeLength));
    }

    public void setLifeLength(FloatValue lifeLength) {
        this.lifeLength = lifeLength;
    }

    public void setInitialParticles(int initialParticles) {
        setInitialParticles(new StaticFloatValue(initialParticles));
    }

    public void setInitialParticles(FloatValue initialParticles) {
        this.initialParticles = initialParticles;
    }

    public void setParticlesPerSecond(float particlesPerSecond) {
        setParticlesPerSecond(new StaticFloatValue(particlesPerSecond));
    }

    public void setParticlesPerSecond(FloatValue particlesPerSecond) {
        this.particlesPerSecond = particlesPerSecond;
    }

    public void setPropertyGenerator(String propertyName, PropertyGenerator propertyGenerator) {
        propertyGenerators.put(propertyName, propertyGenerator);
    }

    @Override
    public void initialCreateParticles(float currentTime, ParticleCreateCallback createCallback) {
        // This is first invocation after start
        int particlesToGenerate = MathUtils.floor(initialParticles.getValue(MathUtils.random()));
        for (int i = 0; i < particlesToGenerate; i++) {
            generateParticle(currentTime, createCallback);
        }
        lastParticleGenerated = currentTime;
    }

    @Override
    public void createParticles(float currentTime, ParticleCreateCallback createCallback) {
        float timeElapsed = currentTime - lastParticleGenerated;
        float particleDelay = 1 / particlesPerSecond.getValue(MathUtils.random());
        int particleCount = MathUtils.floor(timeElapsed / particleDelay);
        for (int i = 0; i < particleCount; i++) {
            generateParticle(lastParticleGenerated + particleDelay * (i + 1), createCallback);
        }
        lastParticleGenerated += particleDelay * particleCount;
    }

    private void generateParticle(float particleBirth, ParticleCreateCallback createCallback) {
        tempPropertyContainer.clear();

        float seed = MathUtils.random();
        float lifeLengthValue = lifeLength.getValue(seed);
        for (ObjectMap.Entry<String, PropertyGenerator> propertyGenerator : propertyGenerators) {
            tempPropertyContainer.setValue(propertyGenerator.key, propertyGenerator.value.generateProperty(seed));
        }

        createCallback.createParticle(particleBirth, lifeLengthValue, tempPropertyContainer);
    }
}

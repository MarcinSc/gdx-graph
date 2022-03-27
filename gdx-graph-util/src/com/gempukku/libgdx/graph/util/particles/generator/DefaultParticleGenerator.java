package com.gempukku.libgdx.graph.util.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.StaticFloatValue;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class DefaultParticleGenerator<T> implements ParticleGenerator<T> {
    private TimeProvider timeProvider;
    private FloatValue lifeLength;
    private FloatValue initialParticles;
    private FloatValue particlesPerSecond;
    private ParticleDataGenerator<T> particleDataGenerator;
    private String positionAttribute = "Position";
    private PositionGenerator positionGenerator;
    private Vector3 tmpVector = new Vector3();

    private float lastParticleGenerated;

    public DefaultParticleGenerator(TimeProvider timeProvider, float lifeLength,
                                    int initialParticles, float particlesPerSecond) {
        this(timeProvider, new StaticFloatValue(lifeLength), new StaticFloatValue(initialParticles), new StaticFloatValue(particlesPerSecond));
    }

    public DefaultParticleGenerator(TimeProvider timeProvider, float minLifeLength, float maxLifeLength,
                                    int initialParticles, float particlesPerSecond) {
        this(timeProvider, new RangeFloatValue(minLifeLength, maxLifeLength), new StaticFloatValue(initialParticles),
                new StaticFloatValue(particlesPerSecond));
    }

    public DefaultParticleGenerator(TimeProvider timeProvider, FloatValue lifeLength,
                                    FloatValue initialParticles, FloatValue particlesPerSecond) {
        this(timeProvider, lifeLength, initialParticles, particlesPerSecond, null);
    }

    public DefaultParticleGenerator(TimeProvider timeProvider, FloatValue lifeLength,
                                    FloatValue initialParticles, FloatValue particlesPerSecond,
                                    ParticleDataGenerator<T> particleDataGenerator) {
        this.timeProvider = timeProvider;
        this.lifeLength = lifeLength;
        this.initialParticles = initialParticles;
        this.particlesPerSecond = particlesPerSecond;
        this.particleDataGenerator = particleDataGenerator;
    }

    public void setPositionAttribute(String positionAttribute) {
        this.positionAttribute = positionAttribute;
    }

    public void setPositionGenerator(PositionGenerator positionGenerator) {
        this.positionGenerator = positionGenerator;
    }

    @Override
    public void initialCreateParticles(ParticleCreateCallback<T> createCallback) {
        float currentTime = timeProvider.getTime();
        // This is first invocation after start
        int particlesToGenerate = MathUtils.floor(initialParticles.getValue(MathUtils.random()));
        for (int i = 0; i < particlesToGenerate; i++) {
            generateParticle(currentTime, createCallback);
        }
        lastParticleGenerated = currentTime;
    }

    @Override
    public void createParticles(ParticleCreateCallback<T> createCallback) {
        float currentTime = timeProvider.getTime();
        float timeElapsed = currentTime - lastParticleGenerated;
        float particleDelay = 1 / particlesPerSecond.getValue(MathUtils.random());
        int particleCount = MathUtils.floor(timeElapsed / particleDelay);
        for (int i = 0; i < particleCount; i++) {
            generateParticle(lastParticleGenerated + particleDelay * (i + 1), createCallback);
        }
        lastParticleGenerated += particleDelay * particleCount;
    }

    private ObjectMap<String, Object> attributes = new ObjectMap<>();

    private void generateParticle(float particleBirth, ParticleCreateCallback<T> createCallback) {
        attributes.clear();

        float lifeLengthValue = lifeLength.getValue(MathUtils.random());
        T particleData = null;
        if (particleDataGenerator != null)
            particleData = particleDataGenerator.generateData();
        if (positionGenerator != null)
            attributes.put(positionAttribute, positionGenerator.generateLocation(tmpVector));
        generateAttributes(attributes);

        createCallback.createParticle(particleBirth, lifeLengthValue, particleData, attributes);
    }

    protected void generateAttributes(ObjectMap<String, Object> attributes) {

    }
}

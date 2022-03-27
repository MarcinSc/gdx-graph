package com.gempukku.libgdx.graph.plugin.particles.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.*;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.model.ParticleModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffectImpl implements GraphParticleEffect, Disposable {
    private final String tag;
    private final ParticleEffectConfiguration particleEffectConfiguration;
    private final RenderableParticleEffect renderableParticleEffect;
    private final ParticleModel particleModel;
    private final ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private boolean running = false;

    private final Array<ParticlesDataContainer> particlesData = new Array<>();
    private int nextParticleIndex = 0;

    private boolean initialParticles;

    public GraphParticleEffectImpl(String tag, ParticleEffectConfiguration particleEffectConfiguration,
                                   RenderableParticleEffect renderableParticleEffect, ParticleModel particleModel,
                                   boolean storeParticleData) {
        this.tag = tag;
        this.particleEffectConfiguration = particleEffectConfiguration;
        this.renderableParticleEffect = renderableParticleEffect;
        this.particleModel = particleModel;

        initializeBuffers(particleEffectConfiguration, storeParticleData);
    }

    public PropertyContainer getPropertyContainer() {
        return renderableParticleEffect.getPropertyContainer(tag);
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public RenderableParticleEffect getRenderableParticleEffect() {
        return renderableParticleEffect;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void initializeBuffers(ParticleEffectConfiguration particleEffectConfiguration, boolean storeParticleData) {
        int maxNumberOfParticlesPerContainer = ((256 * 256) / particleModel.getVertexCount()) - 1;
        int particlesToCreate = particleEffectConfiguration.getMaxNumberOfParticles();
        while (particlesToCreate > 0) {
            int containerCount = Math.min(particlesToCreate, maxNumberOfParticlesPerContainer);
            particlesData.add(new ParticlesDataContainer(particleEffectConfiguration.getVertexAttributes(),
                    particleModel, particleEffectConfiguration.getProperties(),
                    containerCount, storeParticleData));
            particlesToCreate -= containerCount;
        }
    }

    public void generateParticles() {
        if (running) {
            if (initialParticles) {
                renderableParticleEffect.getParticleGenerator(tag).initialCreateParticles(callback);
                initialParticles = false;
            } else {
                renderableParticleEffect.getParticleGenerator(tag).createParticles(callback);
            }
        }
    }

    private ParticlesDataContainer findContainerForIndex(int particleIndex) {
        int skipped = 0;
        for (ParticlesDataContainer particlesDatum : particlesData) {
            int numberOfParticles = particlesDatum.getNumberOfParticles();
            if (numberOfParticles > particleIndex - skipped) {
                return particlesDatum;
            } else {
                skipped += numberOfParticles;
            }
        }
        return null;
    }

    public void render(ParticlesGraphShader graphShader, ShaderContext shaderContext) {
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.applyPendingChanges();
        }
        float currentTime = shaderContext.getTimeProvider().getTime();
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.render(graphShader, shaderContext, currentTime);
        }
    }

    public void start() {
        if (running)
            throw new IllegalStateException("Already started");
        running = true;
        initialParticles = true;
    }

    public void update(TimeProvider timeProvider, ParticleUpdater particleUpdater, boolean accessToData) {
        float currentTime = timeProvider.getTime();
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.update(particleUpdater, currentTime, accessToData);
        }
    }

    public void stop() {
        if (!running)
            throw new IllegalStateException("Not started");
        running = false;
    }

    @Override
    public void dispose() {
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.dispose();
        }
    }

    private class ParticleCreateCallbackImpl<T> implements ParticleGenerator.ParticleCreateCallback<T> {
        @Override
        public void createParticle(float particleBirth, float lifeLength, ObjectMap<String, Object> attributes) {
            createParticle(particleBirth, lifeLength, null, attributes);
        }

        @Override
        public void createParticle(float particleBirth, float lifeLength, T particleData, ObjectMap<String, Object> attributes) {
            ParticlesDataContainer container = findContainerForIndex(nextParticleIndex);
            container.generateParticle(particleBirth, lifeLength, particleData, attributes);

            nextParticleIndex = (nextParticleIndex + 1) % particleEffectConfiguration.getMaxNumberOfParticles();
        }
    }
}

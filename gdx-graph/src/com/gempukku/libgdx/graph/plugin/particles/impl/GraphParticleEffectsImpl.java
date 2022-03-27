package com.gempukku.libgdx.graph.plugin.particles.impl;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.particles.*;
import com.gempukku.libgdx.graph.plugin.particles.model.ParticleModel;
import com.gempukku.libgdx.graph.plugin.particles.model.QuadParticleModel;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffectsImpl implements GraphParticleEffects, RuntimePipelinePlugin, Disposable {
    private final ObjectSet<GraphParticleEffectImpl> particleEffects = new ObjectSet<>();
    private final ObjectMap<String, ObjectSet<GraphParticleEffectImpl>> effectsByTag = new ObjectMap<>();
    private final ObjectMap<String, ParticleEffectConfiguration> effectsConfiguration = new ObjectMap<>();
    private final ObjectMap<String, MapWritablePropertyContainer> globalProperties = new ObjectMap<>();
    private TimeProvider timeProvider;

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void registerEffect(String tag, ParticlesGraphShader shader) {
        if (effectsConfiguration.containsKey(tag))
            throw new IllegalStateException("Duplicate particle effect with tag - " + tag);
        effectsConfiguration.put(tag, new ParticleEffectConfiguration(shader.getVertexAttributes(), shader.getProperties(),
                shader.getMaxNumberOfParticles()));
        globalProperties.put(tag, new MapWritablePropertyContainer());
        effectsByTag.put(tag, new ObjectSet<GraphParticleEffectImpl>());
    }

    public Iterable<GraphParticleEffectImpl> getParticleEffects(String tag) {
        return effectsByTag.get(tag);
    }

    @Override
    public void update(TimeProvider timeProvider) {
        setTimeProvider(timeProvider);
        for (GraphParticleEffectImpl particleEffect : particleEffects) {
            particleEffect.generateParticles();
        }
    }

    @Override
    public GraphParticleEffect createEffect(String tag, RenderableParticleEffect particleEffect) {
        return createEffect(tag, particleEffect, null, new QuadParticleModel());
    }

    @Override
    public GraphParticleEffect createEffect(String tag, RenderableParticleEffect particleEffect, ParticleModel particleModel) {
        return createEffect(tag, particleEffect, null, particleModel);
    }

    @Override
    public <T> GraphParticleEffect createEffect(String tag, RenderableParticleEffect<T> particleEffect, Class<T> particleDataClass) {
        return createEffect(tag, particleEffect, particleDataClass, new QuadParticleModel());
    }

    @Override
    public <T> GraphParticleEffect createEffect(String tag, RenderableParticleEffect<T> particleEffect, Class<T> particleDataClass,
                                                ParticleModel particleModel) {
        ParticleEffectConfiguration configuration = effectsConfiguration.get(tag);
        if (configuration == null)
            throw new IllegalArgumentException("Unable to find particle effect with tag - " + tag);

        GraphParticleEffectImpl result = new GraphParticleEffectImpl(tag, configuration, particleEffect, particleModel, particleDataClass != null);
        particleEffects.add(result);
        effectsByTag.get(tag).add(result);
        return result;
    }

    @Override
    public void startEffect(GraphParticleEffect effect) {
        getEffect(effect).start();
    }

    @Override
    public void updateParticles(GraphParticleEffect effect, ParticleUpdater particleUpdater) {
        updateParticles(effect, particleUpdater, null);
    }

    @Override
    public <T> void updateParticles(GraphParticleEffect effect, ParticleUpdater<T> particleUpdater, Class<T> particleDataClass) {
        getEffect(effect).update(timeProvider, particleUpdater, particleDataClass != null);
    }

    @Override
    public void stopEffect(GraphParticleEffect effect) {
        getEffect(effect).stop();
    }

    @Override
    public void destroyEffect(GraphParticleEffect effect) {
        GraphParticleEffectImpl effectImpl = getEffect(effect);
        particleEffects.remove(effectImpl);
        effectsByTag.get(effect.getTag()).remove(effectImpl);
        effectImpl.dispose();
    }

    @Override
    public void setGlobalProperty(String tag, String name, Object value) {
        globalProperties.get(tag).setValue(name, value);
    }

    @Override
    public void unsetGlobalProperty(String tag, String name) {
        globalProperties.get(tag).remove(name);
    }

    @Override
    public Object getGlobalProperty(String tag, String name) {
        return globalProperties.get(tag).getValue(name);
    }

    public PropertyContainer getGlobalPropertyContainer(String tag) {
        return globalProperties.get(tag);
    }

    @Override
    public void dispose() {
        for (GraphParticleEffectImpl value : particleEffects) {
            value.dispose();
        }
    }

    private GraphParticleEffectImpl getEffect(GraphParticleEffect effect) {
        GraphParticleEffectImpl effectImpl = (GraphParticleEffectImpl) effect;
        if (!particleEffects.contains(effectImpl))
            throw new IllegalArgumentException("Unable to find the graph particle effect");
        return effectImpl;
    }

    public boolean hasEffects(String tag) {
        return !effectsByTag.get(tag).isEmpty();
    }
}

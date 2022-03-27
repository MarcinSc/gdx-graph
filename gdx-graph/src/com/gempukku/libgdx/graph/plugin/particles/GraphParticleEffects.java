package com.gempukku.libgdx.graph.plugin.particles;

import com.gempukku.libgdx.graph.plugin.particles.model.ParticleModel;

/**
 * Main interface that is used to work with particle effects. Any operation done on the particle effects should
 * be done through this interface.
 */
public interface GraphParticleEffects {
    /**
     * Creates a particle effect with the provided information. Note, that an effect gets created in an un-started
     * state, and will not display anything until it is started.
     * <p>
     * The effect is created using a quad model.
     *
     * @param tag
     * @param particleEffect
     * @return
     */
    GraphParticleEffect createEffect(String tag, RenderableParticleEffect particleEffect);

    /**
     * Creates a particle effect with the provided information. Note, that an effect gets created in an un-started
     * state, and will not display anything until it is started.
     * <p>
     * The effect is created using the provided particle model.
     *
     * @param tag
     * @param particleEffect
     * @param particleModel
     * @return
     */
    GraphParticleEffect createEffect(String tag, RenderableParticleEffect particleEffect, ParticleModel particleModel);

    /**
     * Creates a particle effect with the provided information. Note, that an effect gets created in an un-started
     * state, and will not display anything until it is started.
     * <p>
     * The effect is created using a quad model.
     *
     * @param tag
     * @param particleEffect
     * @param particleDataClass
     * @param <T>
     * @return
     */
    <T> GraphParticleEffect createEffect(String tag, RenderableParticleEffect<T> particleEffect, Class<T> particleDataClass);

    /**
     * Creates a particle effect with the provided information. Note, that an effect gets created in an un-started
     * state, and will not display anything until it is started.
     * <p>
     * The effect is created using the provided particle model.
     *
     * @param tag
     * @param particleEffect
     * @param particleDataClass
     * @param particleModel
     * @param <T>
     * @return
     */
    <T> GraphParticleEffect createEffect(String tag, RenderableParticleEffect<T> particleEffect, Class<T> particleDataClass,
                                         ParticleModel particleModel);

    /**
     * Starts generation of particles for the identified effect.
     *
     * @param effect The effect object.
     */
    void startEffect(GraphParticleEffect effect);

    /**
     * Update all particles in the identified effect. This will not provide access to reading per-particle datathe per-particle data.
     *
     * @param effect          The effect object.
     * @param particleUpdater A class that is called to update each particle.
     */
    void updateParticles(GraphParticleEffect effect, ParticleUpdater particleUpdater);

    /**
     * Update all particles in the identified effect. This method allows access to the particle data.
     *
     * @param effect            The effect object.
     * @param particleUpdater   A class that is called to update each particle.
     * @param particleDataClass Class used for storing per-particle data.
     * @param <T>               Class used for particle data
     */
    <T> void updateParticles(GraphParticleEffect effect, ParticleUpdater<T> particleUpdater, Class<T> particleDataClass);

    /**
     * Stops generation of particles for the identified effect.
     *
     * @param effect The effect object.
     */
    void stopEffect(GraphParticleEffect effect);

    /**
     * Destroys the identified effect.
     *
     * @param effect The effect object.
     */
    void destroyEffect(GraphParticleEffect effect);

    void setGlobalProperty(String tag, String name, Object value);

    void unsetGlobalProperty(String tag, String name);

    Object getGlobalProperty(String tag, String name);
}

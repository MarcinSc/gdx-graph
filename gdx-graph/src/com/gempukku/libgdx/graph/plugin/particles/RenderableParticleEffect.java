package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;

public interface RenderableParticleEffect<T> {
    /**
     * Checks if this effect should be rendered. This method could be used for frustum culling,
     * or as a very quick method for making effect not being rendered.
     *
     * @param camera
     * @param tag
     * @return
     */
    boolean isRendered(Camera camera, String tag);

    /**
     * Should return properties of the effect for a given 'tag' (shader).
     *
     * @param tag
     * @return
     */
    PropertyContainer getPropertyContainer(String tag);

    /**
     * Returns a particle generator used to generate particles.
     *
     * @param tag
     * @return
     */
    ParticleGenerator<T> getParticleGenerator(String tag);
}

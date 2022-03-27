package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.RenderableParticleEffect;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class CommonPropertiesRenderableParticleEffect<T> implements RenderableParticleEffect<T> {
    private Vector3 position;
    private ParticleGenerator<T> particleGenerator;
    private WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    public CommonPropertiesRenderableParticleEffect(ParticleGenerator<T> particleGenerator) {
        this(particleGenerator, new MapWritablePropertyContainer());
    }

    public CommonPropertiesRenderableParticleEffect(Vector3 position, ParticleGenerator<T> particleGenerator) {
        this.position = position;
        this.particleGenerator = particleGenerator;
    }

    public CommonPropertiesRenderableParticleEffect(ParticleGenerator<T> particleGenerator, WritablePropertyContainer propertyContainer) {
        this.particleGenerator = particleGenerator;
        this.propertyContainer = propertyContainer;
    }

    public CommonPropertiesRenderableParticleEffect(Vector3 position, ParticleGenerator<T> particleGenerator, WritablePropertyContainer propertyContainer) {
        this.position = position;
        this.particleGenerator = particleGenerator;
        this.propertyContainer = propertyContainer;
    }

    public void setParticleGenerator(ParticleGenerator<T> particleGenerator) {
        this.particleGenerator = particleGenerator;
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    public Vector3 getPosition() {
        return position;
    }

    @Override
    public boolean isRendered(Camera camera, String tag) {
        return cullingTest == null || !cullingTest.isCulled(camera, getPosition());
    }

    @Override
    public WritablePropertyContainer getPropertyContainer(String tag) {
        return propertyContainer;
    }

    @Override
    public ParticleGenerator<T> getParticleGenerator(String tag) {
        return particleGenerator;
    }
}

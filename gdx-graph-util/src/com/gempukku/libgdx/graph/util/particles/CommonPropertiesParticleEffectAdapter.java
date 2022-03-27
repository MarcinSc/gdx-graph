package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.GraphParticleEffect;
import com.gempukku.libgdx.graph.plugin.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.plugin.particles.RenderableParticleEffect;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class CommonPropertiesParticleEffectAdapter<T> {
    private GraphParticleEffects graphParticleEffects;
    private Vector3 position = new Vector3();
    private WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;
    private ObjectMap<String, GraphParticleEffect> tagParticleEffects = new ObjectMap<>();
    private ObjectMap<String, ParticleGenerator<T>> tagParticleGenerators = new ObjectMap<>();

    private RenderableParticleEffectImpl particleEffect = new RenderableParticleEffectImpl();

    public CommonPropertiesParticleEffectAdapter(GraphParticleEffects graphParticleEffects) {
        this(graphParticleEffects, Vector3.Zero);
    }

    public CommonPropertiesParticleEffectAdapter(GraphParticleEffects graphParticleEffects, Vector3 position) {
        this(graphParticleEffects, position, null);
    }

    public CommonPropertiesParticleEffectAdapter(GraphParticleEffects graphParticleEffects, Vector3 position, CullingTest cullingTest) {
        this(graphParticleEffects, position, cullingTest, new MapWritablePropertyContainer());
    }

    public CommonPropertiesParticleEffectAdapter(GraphParticleEffects graphParticleEffects, Vector3 position, CullingTest cullingTest, WritablePropertyContainer propertyContainer) {
        this.graphParticleEffects = graphParticleEffects;
        this.position.set(position);
        this.cullingTest = cullingTest;
        this.propertyContainer = propertyContainer;
    }

    public void startEffect(String tag) {
        graphParticleEffects.startEffect(tagParticleEffects.get(tag));
    }

    public void stopEffect(String tag) {
        graphParticleEffects.stopEffect(tagParticleEffects.get(tag));
    }

    public void addTag(String tag, ParticleGenerator<T> particleGenerator) {
        if (!hasTag(tag)) {
            tagParticleEffects.put(tag, graphParticleEffects.createEffect(tag, particleEffect));
            tagParticleGenerators.put(tag, particleGenerator);
        }
    }

    public void removeTag(String tag) {
        if (hasTag(tag)) {
            graphParticleEffects.destroyEffect(tagParticleEffects.remove(tag));
            tagParticleGenerators.remove(tag);
        }
    }

    public boolean hasTag(String tag) {
        return tagParticleEffects.containsKey(tag);
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    public Vector3 getPosition() {
        return position;
    }

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private class RenderableParticleEffectImpl implements RenderableParticleEffect<T> {
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
            return tagParticleGenerators.get(tag);
        }
    }
}

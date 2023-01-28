package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.sprite.ObjectReference;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.manager.MultiPageObjectBatchModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.ContinuousSlotsObjectMeshStorage;
import com.gempukku.libgdx.graph.util.sprite.storage.DefaultSpriteSerializer;

public class ParticleModel implements Disposable {
    private final ObjectSet<ParticleGenerator> particleGenerators = new ObjectSet<>();
    private final ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private final ParticlesSpriteBatchProducer spriteModelManager;
    private final MultiPageObjectBatchModel<RenderableSprite, ObjectReference, ParticleObjectRenderableModel<RenderableSprite, ObjectReference>> spriteBatchModel;
    private ParticleObjectRenderableModel<RenderableSprite, ObjectReference> lastSpriteModel;
    private MapWritablePropertyContainer propertyContainer;

    public ParticleModel(int particlesPerPage, GraphModels graphModels, String tag) {
        this(particlesPerPage, new QuadSpriteModel(), graphModels, tag);
    }

    public ParticleModel(int particlesPerPage, SpriteModel spriteModel, GraphModels graphModels, String tag) {
        propertyContainer = new MapWritablePropertyContainer();
        spriteModelManager = new ParticlesSpriteBatchProducer(particlesPerPage, spriteModel, graphModels, tag);
        spriteBatchModel = new MultiPageObjectBatchModel<>(spriteModelManager);
    }

    public void addGenerator(float currentTime, ParticleGenerator generator) {
        particleGenerators.add(generator);
        generator.initialCreateParticles(currentTime, callback);
    }

    public void removeGenerator(ParticleGenerator generator) {
        particleGenerators.remove(generator);
    }

    public WritablePropertyContainer getPropertyContainer() {
        return spriteBatchModel.getPropertyContainer();
    }

    public void update(float currentTime) {
        for (ParticleGenerator particleGenerator : particleGenerators) {
            particleGenerator.createParticles(currentTime, callback);
        }
        spriteModelManager.removeSpritesFromOldPages(currentTime);
    }

    private void createParticleImpl(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
        spriteBatchModel.addObject(new ParticleRenderableSprite(particleBirth, lifeLength, propertyContainer));
        lastSpriteModel.updateWithMaxDeathTime(particleBirth + lifeLength);
    }

    private class ParticleCreateCallbackImpl implements ParticleGenerator.ParticleCreateCallback {
        @Override
        public void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
            createParticleImpl(particleBirth, lifeLength, propertyContainer);
        }
    }

    @Override
    public void dispose() {
        spriteBatchModel.dispose();
    }

    private class ParticlesSpriteBatchProducer implements DisposableProducer<ParticleObjectRenderableModel<RenderableSprite, ObjectReference>> {
        private final int spriteCapacity;
        private final SpriteModel spriteModel;
        private final GraphModels graphModels;
        private final String tag;

        private final ObjectSet<ParticleObjectRenderableModel<RenderableSprite, ObjectReference>> models = new ObjectSet<>();
        private final VertexAttributes vertexAttributes;
        private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;
        private final DefaultSpriteSerializer spriteSerializer;

        public ParticlesSpriteBatchProducer(int spriteCapacity,
                                            SpriteModel spriteModel,
                                            GraphModels graphModels, String tag) {
            this.spriteCapacity = spriteCapacity;
            this.spriteModel = spriteModel;
            this.graphModels = graphModels;
            this.tag = tag;

            vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
            vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

            spriteSerializer = new DefaultSpriteSerializer(
                    vertexAttributes, vertexPropertySources, spriteModel);
        }

        @Override
        public ParticleObjectRenderableModel create() {
            ParticleObjectRenderableModel<RenderableSprite, ObjectReference> model = new ParticleObjectRenderableModel<RenderableSprite, ObjectReference>(
                    new ContinuousSlotsObjectMeshStorage<RenderableSprite>(spriteCapacity, vertexAttributes.vertexSize / 4,
                            spriteModel, spriteSerializer), vertexAttributes, propertyContainer);
            lastSpriteModel = model;
            models.add(model);
            graphModels.addModel(tag, model);
            return model;
        }

        @Override
        public void dispose(ParticleObjectRenderableModel<RenderableSprite, ObjectReference> model) {
            graphModels.removeModel(tag, model);
            model.dispose();
            models.remove(model);
        }

        public void removeSpritesFromOldPages(float currentTime) {
            Array<ParticleObjectRenderableModel<RenderableSprite, ObjectReference>> modelsToDispose = new Array<>();
            for (ParticleObjectRenderableModel<RenderableSprite, ObjectReference> model : models) {
                if (model.getMaxDeathTime() < currentTime) {
                    modelsToDispose.add(model);
                }
            }

            for (ParticleObjectRenderableModel<RenderableSprite, ObjectReference> particleSpriteRenderableModel : modelsToDispose) {
                spriteBatchModel.disposeOfPage(particleSpriteRenderableModel);
            }

        }
    }
}

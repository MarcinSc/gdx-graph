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
import com.gempukku.libgdx.graph.util.Producer;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMeshStorage;
import com.gempukku.libgdx.graph.util.storage.MultiPageObjectBatchModel;

public class ParticleModel implements Disposable {
    private final ObjectSet<ParticleGenerator> particleGenerators = new ObjectSet<>();
    private final ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private final ParticlesSpriteBatchProducer spriteModelManager;
    private final MultiPageObjectBatchModel<RenderableSprite, SpriteReference> spriteBatchModel;
    private final MapWritablePropertyContainer propertyContainer;

    private ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> lastSpriteModel;

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

    private class ParticlesSpriteBatchProducer implements DisposableProducer<ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference>> {
        private final int spriteCapacity;
        private final SpriteModel spriteModel;
        private final GraphModels graphModels;
        private final String tag;

        private final ObjectSet<ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference>> models = new ObjectSet<>();
        private final VertexAttributes vertexAttributes;
        private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;
        private final SpriteSerializer spriteSerializer;

        public ParticlesSpriteBatchProducer(int spriteCapacity,
                                            SpriteModel spriteModel,
                                            GraphModels graphModels, String tag) {
            this.spriteCapacity = spriteCapacity;
            this.spriteModel = spriteModel;
            this.graphModels = graphModels;
            this.tag = tag;

            vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
            vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

            spriteSerializer = new SpriteSerializer(
                    vertexAttributes, vertexPropertySources, spriteModel);
        }

        @Override
        public ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> create() {
            ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> model = new ParticleObjectRenderableModelGdx<>(
                    new SpriteSlotMeshStorage<>(spriteCapacity,
                            spriteModel, spriteSerializer,
                            new Producer<SpriteReference>() {
                                @Override
                                public SpriteReference create() {
                                    return new SpriteReference();
                                }
                            }), vertexAttributes, propertyContainer);
            lastSpriteModel = model;
            models.add(model);
            graphModels.addModel(tag, model);
            return model;
        }

        @Override
        public void dispose(ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> model) {
            graphModels.removeModel(tag, model);
            model.dispose();
            models.remove(model);
        }

        public void removeSpritesFromOldPages(float currentTime) {
            Array<ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference>> modelsToDispose = new Array<>();
            for (ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> model : models) {
                if (model.getMaxDeathTime() < currentTime) {
                    modelsToDispose.add(model);
                }
            }

            for (ParticleObjectRenderableModelGdx<RenderableSprite, SpriteReference> particleSpriteRenderableModel : modelsToDispose) {
                spriteBatchModel.disposeOfPage(particleSpriteRenderableModel);
            }
        }
    }
}

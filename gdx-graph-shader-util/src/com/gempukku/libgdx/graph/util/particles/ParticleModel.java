package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.util.DisposableProducer;
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMemoryMesh;
import com.gempukku.libgdx.graph.util.storage.GdxMeshRenderableModel;
import com.gempukku.libgdx.graph.util.storage.PagedMultiPartBatchModel;

public class ParticleModel implements Disposable {
    private final ObjectSet<ParticleGenerator> particleGenerators = new ObjectSet<>();
    private final ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private final ParticlesSpriteBatchProducer spriteModelManager;
    private final PagedMultiPartBatchModel<RenderableSprite, SpriteReference> spriteBatchModel;
    private final MapWritablePropertyContainer propertyContainer;

    private ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> lastSpriteModel;

    public ParticleModel(int particlesPerPage, GraphModels graphModels, String tag) {
        this(particlesPerPage, new QuadSpriteModel(), graphModels, tag);
    }

    public ParticleModel(int particlesPerPage, SpriteModel spriteModel, GraphModels graphModels, String tag) {
        propertyContainer = new MapWritablePropertyContainer();
        spriteModelManager = new ParticlesSpriteBatchProducer(particlesPerPage, spriteModel, graphModels, tag);
        spriteBatchModel = new PagedMultiPartBatchModel<>(spriteModelManager);
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
        spriteBatchModel.addPart(new ParticleRenderableSprite(particleBirth, lifeLength, propertyContainer));
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

    private class ParticlesSpriteBatchProducer implements DisposableProducer<ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference>> {
        private final int spriteCapacity;
        private final SpriteModel spriteModel;
        private final GraphModels graphModels;
        private final String tag;

        private final ObjectSet<ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference>> models = new ObjectSet<>();
        private final VertexAttributes vertexAttributes;
        private final SpriteSerializer spriteSerializer;

        public ParticlesSpriteBatchProducer(int spriteCapacity,
                                            SpriteModel spriteModel,
                                            GraphModels graphModels, String tag) {
            this.spriteCapacity = spriteCapacity;
            this.spriteModel = spriteModel;
            this.graphModels = graphModels;
            this.tag = tag;

            vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
            ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

            spriteSerializer = new SpriteSerializer(
                    vertexAttributes, vertexPropertySources, spriteModel);
        }

        @Override
        public ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> create() {
            SpriteSlotMemoryMesh<RenderableSprite> meshModel =
                    new SpriteSlotMemoryMesh<>(spriteCapacity,
                            spriteModel, spriteSerializer);
            GdxMeshRenderableModel gdxMesh = new GdxMeshRenderableModel(false, meshModel,
                    vertexAttributes, propertyContainer, tag);
            ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> model =
                    new ParticleMultiPartRenderableModelGdx<>(meshModel, gdxMesh);
            lastSpriteModel = model;
            models.add(model);
            graphModels.addModel(model);
            return model;
        }

        @Override
        public void dispose(ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> model) {
            graphModels.removeModel(model);
            model.dispose();
            models.remove(model);
        }

        public void removeSpritesFromOldPages(float currentTime) {
            Array<ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference>> modelsToDispose = new Array<>();
            for (ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> model : models) {
                if (model.getMaxDeathTime() < currentTime) {
                    modelsToDispose.add(model);
                }
            }

            for (ParticleMultiPartRenderableModelGdx<RenderableSprite, SpriteReference> particleSpriteRenderableModel : modelsToDispose) {
                spriteBatchModel.disposeOfPage(particleSpriteRenderableModel);
            }
        }
    }
}

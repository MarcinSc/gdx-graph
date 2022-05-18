package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.sprite.MultiPageSpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.manager.SpriteRenderableModelManager;

public class ParticleModel implements Disposable {
    private final ObjectSet<ParticleGenerator> particleGenerators = new ObjectSet<>();
    private final ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private final ParticlesSpriteRenderableModelManager spriteModelManager;
    private final MultiPageSpriteBatchModel spriteBatchModel;
    private ParticleSpriteRenderableModel lastSpriteModel;

    public ParticleModel(int spriteCapacity, GraphModels graphModels, String tag) {
        spriteModelManager = new ParticlesSpriteRenderableModelManager(false, spriteCapacity, graphModels, tag);
        spriteBatchModel = new MultiPageSpriteBatchModel(spriteModelManager);
    }

    public void addGenerator(float currentTime, ParticleGenerator generator) {
        particleGenerators.add(generator);
        generator.initialCreateParticles(currentTime, callback);
    }

    public void removeGenerator(ParticleGenerator generator) {
        particleGenerators.remove(generator);
    }

    public void update(float currentTime) {
        for (ParticleGenerator particleGenerator : particleGenerators) {
            particleGenerator.createParticles(currentTime, callback);
        }
        spriteModelManager.removeSpritesFromOldPages(currentTime);
        spriteBatchModel.disposeEmptyPages();
    }

    private void createParticleImpl(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
        spriteBatchModel.addSprite(new ParticleRenderableSprite(particleBirth, lifeLength, propertyContainer));
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

    private class ParticlesSpriteRenderableModelManager implements SpriteRenderableModelManager<ParticleSpriteRenderableModel> {
        private final boolean staticBatch;
        private final int spriteCapacity;
        private final GraphModels graphModels;
        private final String tag;

        private final ObjectSet<ParticleSpriteRenderableModel> models = new ObjectSet<>();
        private final VertexAttributes vertexAttributes;
        private final ObjectMap<VertexAttribute, PropertySource> vertexPropertySources;

        public ParticlesSpriteRenderableModelManager(boolean staticBatch, int spriteCapacity,
                                                     GraphModels graphModels, String tag) {
            this.staticBatch = staticBatch;
            this.spriteCapacity = spriteCapacity;
            this.graphModels = graphModels;
            this.tag = tag;

            vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
            vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);
        }

        @Override
        public ParticleSpriteRenderableModel createNewModel(WritablePropertyContainer propertyContainer) {
            ParticleSpriteRenderableModel model = new ParticleSpriteRenderableModel(staticBatch, spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer);
            lastSpriteModel = model;
            models.add(model);
            graphModels.addModel(tag, model);
            return model;
        }

        @Override
        public boolean shouldDisposeEmptyModel(ParticleSpriteRenderableModel model) {
            return true;
        }

        @Override
        public void disposeModel(ParticleSpriteRenderableModel model) {
            graphModels.removeModel(tag, model);
            model.dispose();
            models.remove(model);
        }

        public void removeSpritesFromOldPages(float currentTime) {
            for (ParticleSpriteRenderableModel model : models) {
                if (model.getMaxDeathTime() < currentTime) {
                    model.removeAllSprites();
                }
            }
        }
    }
}

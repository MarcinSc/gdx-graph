package com.gempukku.libgdx.graph.artemis.particle;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.DefaultSpriteModel;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.util.particles.ParticleModel;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.shape.ShapeSystem;

// Should not be necessary, as we only want to make the ShapeSystem optional, but
// that is what I consider a bug in Artemis, where it doesn't work, if you just
// put it on field.
@Wire(failOnNull = false)
public class ParticleBatchSystem extends BaseEntitySystem {
    private EvaluatePropertySystem evaluatePropertySystem;
    private PipelineRendererSystem pipelineRendererSystem;
    @Wire(failOnNull = false)
    private ShapeSystem shapeSystem;

    private SpriteModel defaultSpriteModel = new QuadSpriteModel();

    private ComponentMapper<ParticleBatchComponent> particleBatchComponentMapper;

    private final ObjectMap<String, ParticleModel> particleModelMap = new ObjectMap<>();

    private Array<Entity> newParticleBatchEntities = new Array<>();

    public ParticleBatchSystem() {
        super(Aspect.all(ParticleBatchComponent.class));
    }

    @Override
    protected void inserted(int entityId) {
        newParticleBatchEntities.add(world.getEntity(entityId));
    }

    public ParticleModel getParticleModel(String particleBatchName) {
        return particleModelMap.get(particleBatchName);
    }

    private ParticleModel createParticleBatchModel(ParticleBatchComponent particleBatch, GraphModels graphModels, String tag) {
        SpriteModel particleSpriteModel = getSpriteModel(particleBatch);
        ParticleModel particleModel = new ParticleModel(particleBatch.getSpritesPerPage(), particleSpriteModel, graphModels, tag);
        for (String particleBirthAttribute : particleBatch.getParticleBirthAttributes()) {
            particleModel.addParticleBirthProperty(particleBirthAttribute);
        }
        for (String particleDeathAttribute : particleBatch.getParticleDeathAttributes()) {
            particleModel.addParticleDeathProperty(particleDeathAttribute);
        }
        return particleModel;
    }

    private SpriteModel getSpriteModel(ParticleBatchComponent particleBatch) {
        String shapeName = particleBatch.getShapeName();
        if (shapeName != null) {
            if (shapeSystem == null)
                throw new GdxRuntimeException("Sprite batch has shape name defined, but no ShapeSystem is in context");

            short[] indices = shapeSystem.getIndices(shapeName);
            int vertexCount = shapeSystem.getVertexCount(shapeName);
            return new DefaultSpriteModel(vertexCount, indices);
        } else {
            return defaultSpriteModel;
        }
    }

    @Override
    protected void processSystem() {
        for (Entity newParticleBatchEntity : newParticleBatchEntities) {
            ParticleBatchComponent particleBatch = particleBatchComponentMapper.get(newParticleBatchEntity);
            GraphModels graphModels = pipelineRendererSystem.getPluginData(GraphModels.class);

            String tag = particleBatch.getRenderTag();
            ParticleModel spriteModel = createParticleBatchModel(particleBatch, graphModels, tag);

            WritablePropertyContainer propertyContainer = spriteModel.getPropertyContainer();
            for (ObjectMap.Entry<String, Object> property : particleBatch.getProperties()) {
                propertyContainer.setValue(property.key, evaluatePropertySystem.evaluateProperty(newParticleBatchEntity, property.value, Object.class));
            }

            particleModelMap.put(particleBatch.getName(), spriteModel);
        }
        newParticleBatchEntities.clear();

        float currentTime = pipelineRendererSystem.getCurrentTime();
        for (ParticleModel particleModel : particleModelMap.values()) {
            particleModel.update(currentTime);
        }
    }

    public void addParticleGenerator(String particleBatchName, ParticleGenerator particleGenerator) {
        particleModelMap.get(particleBatchName).addGenerator(pipelineRendererSystem.getCurrentTime(), particleGenerator);
    }

    public void removeParticleGenerator(String particleBatchName, ParticleGenerator particleGenerator) {
        particleModelMap.get(particleBatchName).removeGenerator(particleGenerator);
    }

    @Override
    public void dispose() {
        for (ParticleModel particleModel : particleModelMap.values()) {
            particleModel.dispose();
        }
        particleModelMap.clear();
    }
}

package com.gempukku.libgdx.graph.artemis.particle;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.particle.property.EntityPositionProperty;
import com.gempukku.libgdx.graph.artemis.particle.property.OwnEntityPositionProperty;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultPropertyGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PropertyGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.util.particles.generator.value.StaticFloatValue;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

public class ParticleEffectSystem extends BaseEntitySystem implements PropertyEvaluator {
    private ParticleBatchSystem particleBatchSystem;
    private EvaluatePropertySystem evaluatePropertySystem;
    private PipelineRendererSystem pipelineRendererSystem;
    private TransformSystem transformSystem;

    private ComponentMapper<ParticleEffectComponent> particleEffectComponentMapper;

    private final IntMap<Array<BatchNameWithParticleGenerator>> effectsMap = new IntMap<>();

    private Array<Entity> newEffectEntities = new Array<>();

    public ParticleEffectSystem() {
        super(Aspect.all(ParticleEffectComponent.class));
    }

    @Override
    protected void initialize() {
        evaluatePropertySystem.addPropertyEvaluator(this);
    }

    public void updateParticleGenerators(int entityId) {
        removed(entityId);
        addParticleEffects(entityId, world.getEntity(entityId), particleEffectComponentMapper.get(entityId));
    }

    public void updateParticleGenerator(int entityId, int particleEffectIndex) {
        DefaultParticleGenerator particleGenerator = effectsMap.get(entityId).get(particleEffectIndex).particleGenerator;
        ParticleEffect particleEffectDefinition = particleEffectComponentMapper.get(entityId).getParticleEffects().get(particleEffectIndex);

        FloatValue lifeLength = particleEffectDefinition.getVariableLifeLength();
        if (lifeLength == null)
            lifeLength = new StaticFloatValue(particleEffectDefinition.getLifeLength());
        FloatValue initialParticles = particleEffectDefinition.getVariableInitialParticles();
        if (initialParticles == null)
            initialParticles = new StaticFloatValue(particleEffectDefinition.getInitialParticles());
        FloatValue particlesPerSecond = particleEffectDefinition.getVariableParticlesPerSecond();
        if (particlesPerSecond == null)
            particlesPerSecond = new StaticFloatValue(particleEffectDefinition.getParticlesPerSecond());

        particleGenerator.setLifeLength(lifeLength);
        particleGenerator.setInitialParticles(initialParticles);
        particleGenerator.setParticlesPerSecond(particlesPerSecond);

        Entity entity = world.getEntity(entityId);
        for (ObjectMap.Entry<String, Object> property : particleEffectDefinition.getProperties()) {
            particleGenerator.setPropertyGenerator(property.key, evaluatePropertySystem.evaluateProperty(entity, property.value, PropertyGenerator.class));
        }
    }

    @EventListener
    public void entityTransformUpdated(TransformUpdated transformUpdated, Entity entity) {
        IntBag entityIds = getEntityIds();
        for (int i = 0, size = entityIds.size(); i < size; i++) {
            int entityId = entityIds.get(i);
            ParticleEffectComponent particleEffect = particleEffectComponentMapper.get(entityId);
            Array<ParticleEffect> particleEffects = particleEffect.getParticleEffects();
            for (int effectIndex = 0; effectIndex < particleEffects.size; effectIndex++) {
                ParticleEffect effect = particleEffects.get(effectIndex);
                boolean updateEffect = effectNeedsUpdating(entity, entityId, effect);
                if (updateEffect)
                    updateParticleGenerator(entityId, effectIndex);
            }

        }
    }

    private boolean effectNeedsUpdating(Entity transformUpdatedEntity, int effectEntityId, ParticleEffect effect) {
        boolean isTransformedEntity = transformUpdatedEntity.getId() == effectEntityId;
        for (Object value : effect.getProperties().values()) {
            if (isTransformedEntity && value instanceof OwnEntityPositionProperty) {
                return true;
            }
            if (value instanceof EntityPositionProperty && ((EntityPositionProperty) value).getEntityId() == transformUpdatedEntity.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean evaluatesProperty(Entity entity, EvaluableProperty value) {
        return value instanceof OwnEntityPositionProperty
                || value instanceof EntityPositionProperty;
    }

    @Override
    public Object evaluateValue(Entity entity, EvaluableProperty value) {
        if (value instanceof OwnEntityPositionProperty) {
            return new DefaultPropertyGenerator(transformSystem.getResolvedTransform(entity).getTranslation(new Vector3()));
        } else if (value instanceof EntityPositionProperty) {
            Entity specifiedEntity = world.getEntity(((EntityPositionProperty) value).getEntityId());
            return new DefaultPropertyGenerator(transformSystem.getResolvedTransform(specifiedEntity).getTranslation(new Vector3()));
        }
        return null;
    }

    @Override
    protected void inserted(int entityId) {
        newEffectEntities.add(world.getEntity(entityId));
    }

    @Override
    protected void removed(int entityId) {
        Array<BatchNameWithParticleGenerator> effects = effectsMap.remove(entityId);
        for (BatchNameWithParticleGenerator effect : effects) {
            particleBatchSystem.getParticleModel(effect.batchName).removeGenerator(effect.particleGenerator);
        }
    }

    private DefaultParticleGenerator addParticleEffect(PipelineParticleModel particleBatchModel, Entity entity, ParticleEffect particleEffectDefinition) {
        FloatValue lifeLength = particleEffectDefinition.getVariableLifeLength();
        if (lifeLength == null)
            lifeLength = new StaticFloatValue(particleEffectDefinition.getLifeLength());
        FloatValue initialParticles = particleEffectDefinition.getVariableInitialParticles();
        if (initialParticles == null)
            initialParticles = new StaticFloatValue(particleEffectDefinition.getInitialParticles());
        FloatValue particlesPerSecond = particleEffectDefinition.getVariableParticlesPerSecond();
        if (particlesPerSecond == null)
            particlesPerSecond = new StaticFloatValue(particleEffectDefinition.getParticlesPerSecond());

        DefaultParticleGenerator particleGenerator = new DefaultParticleGenerator(lifeLength, initialParticles, particlesPerSecond);
        for (ObjectMap.Entry<String, Object> property : particleEffectDefinition.getProperties()) {
            particleGenerator.setPropertyGenerator(property.key, evaluatePropertySystem.evaluateProperty(entity, property.value, PropertyGenerator.class));
        }
        particleBatchModel.addGenerator(pipelineRendererSystem.getPipelineTime(particleBatchModel.getPipelineName()), particleGenerator);
        return particleGenerator;
    }

    private void addParticleEffects(int entityId, Entity effectEntity, ParticleEffectComponent effect) {
        Array<BatchNameWithParticleGenerator> effects = new Array<>();
        for (ParticleEffect particleEffectDefinition : effect.getParticleEffects()) {
            String batchName = particleEffectDefinition.getParticleBatchName();
            PipelineParticleModel particleBatchModel = particleBatchSystem.getParticleModel(batchName);
            DefaultParticleGenerator particleGenerator = addParticleEffect(particleBatchModel, effectEntity, particleEffectDefinition);
            effects.add(new BatchNameWithParticleGenerator(batchName, particleGenerator));
        }

        effectsMap.put(entityId, effects);
    }

    @Override
    protected void processSystem() {
        for (Entity newEffectEntity : newEffectEntities) {
            ParticleEffectComponent effect = particleEffectComponentMapper.get(newEffectEntity);
            if (effect != null)
                addParticleEffects(newEffectEntity.getId(), newEffectEntity, effect);
        }
        newEffectEntities.clear();
    }

    @Override
    protected void dispose() {
        for (Array<BatchNameWithParticleGenerator> effectArray : effectsMap.values()) {
            for (BatchNameWithParticleGenerator effect : effectArray) {
                particleBatchSystem.getParticleModel(effect.batchName).removeGenerator(effect.particleGenerator);
            }
        }
        effectsMap.clear();
    }

    public static class BatchNameWithParticleGenerator {
        private String batchName;
        private DefaultParticleGenerator particleGenerator;

        public BatchNameWithParticleGenerator(String batchName, DefaultParticleGenerator particleGenerator) {
            this.batchName = batchName;
            this.particleGenerator = particleGenerator;
        }
    }
}

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
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.util.ShaderInformation;
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

    private final ObjectMap<String, PipelineParticleModel> particleModelMap = new ObjectMap<>();

    private Array<Entity> newParticleBatchEntities = new Array<>();

    public ParticleBatchSystem() {
        super(Aspect.all(ParticleBatchComponent.class));
    }

    @Override
    protected void inserted(int entityId) {
        newParticleBatchEntities.add(world.getEntity(entityId));
    }

    public PipelineParticleModel getParticleModel(String particleBatchName) {
        return particleModelMap.get(particleBatchName);
    }

    private PipelineParticleModel createParticleBatchModel(ParticleBatchComponent particleBatch, ShaderInformation shaderInformation, ModelContainer<RenderableModel> modelContainer, String tag) {
        SpriteModel particleSpriteModel = getSpriteModel(particleBatch);
        PipelineParticleModel particleModel = new PipelineParticleModel(
                particleBatch.getPipelineName(),
                particleBatch.getSpritesPerPage(), particleSpriteModel, shaderInformation, modelContainer, tag);
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
        ShaderInformation shaderInformation = pipelineRendererSystem.getShaderInformation();
        ModelContainer<RenderableModel> modelContainer = pipelineRendererSystem.getModelContainer();

        for (Entity newParticleBatchEntity : newParticleBatchEntities) {
            ParticleBatchComponent particleBatch = particleBatchComponentMapper.get(newParticleBatchEntity);

            String tag = particleBatch.getRenderTag();
            PipelineParticleModel spriteModel = createParticleBatchModel(particleBatch, shaderInformation, modelContainer, tag);

            WritablePropertyContainer propertyContainer = spriteModel.getPropertyContainer();
            for (ObjectMap.Entry<String, Object> property : particleBatch.getProperties()) {
                propertyContainer.setValue(property.key, evaluatePropertySystem.evaluateProperty(newParticleBatchEntity, property.value, Object.class));
            }

            particleModelMap.put(particleBatch.getName(), spriteModel);
        }
        newParticleBatchEntities.clear();

        for (PipelineParticleModel particleModel : particleModelMap.values()) {
            float currentTime = pipelineRendererSystem.getPipelineTime(particleModel.getPipelineName());
            particleModel.update(currentTime);
        }
    }

    public void addParticleGenerator(String particleBatchName, ParticleGenerator particleGenerator) {
        PipelineParticleModel pipelineParticleModel = particleModelMap.get(particleBatchName);
        float pipelineTime = pipelineRendererSystem.getPipelineTime(pipelineParticleModel.getPipelineName());
        pipelineParticleModel.addGenerator(pipelineTime, particleGenerator);
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

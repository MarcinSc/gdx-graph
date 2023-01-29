package com.gempukku.libgdx.graph.artemis.patchwork;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.patchwork.generator.PatchGenerator;
import com.gempukku.libgdx.graph.util.patchwork.GeometryRenderablePatch;
import com.gempukku.libgdx.graph.util.patchwork.PatchReference;
import com.gempukku.libgdx.graph.util.patchwork.RenderablePatch;
import com.gempukku.libgdx.graph.util.storage.MultiPartBatchModel;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

@Wire(failOnNull = false)
public class PatchGeneratorSystem extends BaseEntitySystem {
    private PatchworkSystem patchworkSystem;
    @Wire(failOnNull = false)
    private TransformSystem transformSystem;
    private EvaluatePropertySystem evaluatePropertySystem;

    private ComponentMapper<PatchComponent> patchComponentMapper;

    private ObjectMap<String, PatchGenerator> patchGeneratorMap = new ObjectMap<>();
    private IntMap<PatchworkNameWithPatchReference> patchMap = new IntMap<>();
    private Array<Entity> newPatchEntities = new Array<>();

    public PatchGeneratorSystem() {
        super(Aspect.all(PatchComponent.class));
    }

    public void registerPatchGenerator(String name, PatchGenerator patchGenerator) {
        patchGeneratorMap.put(name, patchGenerator);
    }

    @Override
    protected void inserted(int entityId) {
        Entity spriteEntity = world.getEntity(entityId);
        newPatchEntities.add(spriteEntity);
    }

    @Override
    protected void removed(int entityId) {
        removePatch(entityId);
    }

    private void removePatch(int entityId) {
        PatchworkNameWithPatchReference patch = patchMap.remove(entityId);
        patchworkSystem.getPatchworkModel(patch.patchworkName).removePart(patch.patchReference);
    }

    public void updatePatch(int entityId) {
        Entity patchEntity = world.getEntity(entityId);
        PatchComponent patch = patchComponentMapper.get(entityId);

        PatchworkNameWithPatchReference oldPatch = patchMap.get(entityId);
        if (!oldPatch.patchworkName.equals(patch.getPatchworkName())) {
            removePatch(entityId);
            addPatch(entityId, patchEntity, patch);
        } else {
            RenderablePatch renderablePatch = createRenderablePatch(patchEntity, patch);
            MultiPartBatchModel<RenderablePatch, PatchReference> multiPartBatchModel = patchworkSystem.getPatchworkModel(patch.getPatchworkName());
            oldPatch.patchReference = multiPartBatchModel.updatePart(renderablePatch, oldPatch.patchReference);
        }
    }

    @EventListener
    public void transformUpdated(TransformUpdated transformUpdated, Entity entity) {
        int entityId = entity.getId();
        if (patchMap.containsKey(entityId)) {
            updatePatch(entity.getId());
        }
    }

    @Override
    protected void processSystem() {
        for (Entity newPatchEntity : newPatchEntities) {
            PatchComponent patch = patchComponentMapper.get(newPatchEntity);
            if (patch != null)
                addPatch(newPatchEntity.getId(), newPatchEntity, patch);
        }
        newPatchEntities.clear();
    }

    private void addPatch(int entityId, Entity patchEntity, PatchComponent patch) {
        String patchworkName = patch.getPatchworkName();
        MultiPartBatchModel<RenderablePatch, PatchReference> multiPartBatchModel = patchworkSystem.getPatchworkModel(patchworkName);

        RenderablePatch renderablePatch = createRenderablePatch(patchEntity, patch);

        PatchReference patchReference = multiPartBatchModel.addPart(renderablePatch);

        patchMap.put(entityId, new PatchworkNameWithPatchReference(patchworkName, patchReference));
    }

    private RenderablePatch createRenderablePatch(Entity patchEntity, PatchComponent patch) {
        Matrix4 transform = null;
        if (transformSystem != null)
            transform = transformSystem.getResolvedTransform(patchEntity);

        PatchGenerator patchGenerator = patchGeneratorMap.get(patch.getGenerator());
        PatchGenerator.GeneratedPatch generatedPatch = patchGenerator.generatePatch(transform, patch.getGeneratorData());

        GeometryRenderablePatch renderablePatch = new GeometryRenderablePatch(generatedPatch.getPatchIndices(), generatedPatch.getVertexCount());

        for (ObjectMap.Entry<String, Object> property : generatedPatch.getProperties()) {
            renderablePatch.setValue(property.key, evaluatePropertySystem.evaluateProperty(patchEntity, property.value, Object.class));
        }
        for (ObjectMap.Entry<String, Object> property : patch.getProperties()) {
            renderablePatch.setValue(property.key, evaluatePropertySystem.evaluateProperty(patchEntity, property.value, Object.class));
        }
        return renderablePatch;
    }

    public static class PatchworkNameWithPatchReference {
        private final String patchworkName;
        private PatchReference patchReference;

        public PatchworkNameWithPatchReference(String patchworkName, PatchReference patchReference) {
            this.patchworkName = patchworkName;
            this.patchReference = patchReference;
        }
    }
}

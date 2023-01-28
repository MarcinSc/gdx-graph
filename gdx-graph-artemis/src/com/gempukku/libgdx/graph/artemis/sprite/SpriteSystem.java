package com.gempukku.libgdx.graph.artemis.sprite;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.VectorUtil;
import com.gempukku.libgdx.graph.artemis.sprite.property.SpritePositionProperty;
import com.gempukku.libgdx.graph.artemis.sprite.property.SpriteUVProperty;
import com.gempukku.libgdx.graph.util.sprite.DefaultRenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.storage.ObjectBatchModel;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

public class SpriteSystem extends BaseEntitySystem implements PropertyEvaluator {
    private final IntMap<Array<BatchNameWithSpriteReference>> spriteMap = new IntMap<>();
    private final DefaultRenderableSprite tempRenderableSprite = new DefaultRenderableSprite();

    private ComponentMapper<SpriteComponent> spriteComponentMapper;

    private SpriteBatchSystem spriteBatchSystem;
    private TransformSystem transformSystem;
    private EvaluatePropertySystem evaluatePropertySystem;

    public static final Vector2ValuePerVertex uvAttribute = new Vector2ValuePerVertex(new float[]{0, 1, 1, 1, 0, 0, 1, 0});
    public static final Vector2ValuePerVertex uvAttributeInvertedX = new Vector2ValuePerVertex(new float[]{1, 1, 0, 1, 1, 0, 0, 0});
    public static final Vector2ValuePerVertex uvAttributeInvertedY = new Vector2ValuePerVertex(new float[]{0, 0, 1, 0, 0, 1, 1, 1});
    public static final Vector2ValuePerVertex uvAttributeInvertedBoth = new Vector2ValuePerVertex(new float[]{1, 0, 0, 0, 1, 1, 0, 1});

    private final Matrix4 tempMatrix = new Matrix4();

    private Array<Entity> newSpriteEntities = new Array<>();

    public SpriteSystem() {
        super(Aspect.all(SpriteComponent.class));
    }

    @Override
    protected void initialize() {
        evaluatePropertySystem.addPropertyEvaluator(this);
    }

    public void updateSprites(int entityId) {
        Entity spriteEntity = world.getEntity(entityId);
        SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);

        for (BatchNameWithSpriteReference batchNameWithSpriteIndex : spriteMap.remove(entityId)) {
            spriteBatchSystem.getSpriteBatchModel(batchNameWithSpriteIndex.batchName).removeObject(batchNameWithSpriteIndex.objectReference);
        }

        addSprites(entityId, spriteEntity, sprite);
    }

    public void updateSprite(int entityId, int spriteDefinitionIndex) {
        Entity spriteEntity = world.getEntity(entityId);
        SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
        Array<BatchNameWithSpriteReference> sprites = spriteMap.get(entityId);
        if (sprites != null) {
            BatchNameWithSpriteReference batchNameWithSpriteIndex = sprites.get(spriteDefinitionIndex);

            SpriteReference newObjectReference = updateSprite(spriteBatchSystem.getSpriteBatchModel(batchNameWithSpriteIndex.batchName),
                    spriteEntity, sprite.getSprites().get(spriteDefinitionIndex),
                    batchNameWithSpriteIndex.objectReference);
            batchNameWithSpriteIndex.objectReference = newObjectReference;
        }
    }

    private SpriteReference addSprite(ObjectBatchModel<RenderableSprite, SpriteReference> objectBatchModel, Entity entity, SpriteDefinition spriteDefinition) {
        RenderableSprite renderableSprite = obtainRenderableSprite(entity, spriteDefinition);
        return objectBatchModel.addObject(renderableSprite);
    }

    private SpriteReference updateSprite(ObjectBatchModel<RenderableSprite, SpriteReference> objectBatchModel, Entity entity, SpriteDefinition spriteDefinition, SpriteReference objectReference) {
        RenderableSprite renderableSprite = obtainRenderableSprite(entity, spriteDefinition);
        return objectBatchModel.updateObject(renderableSprite, objectReference);
    }

    private RenderableSprite obtainRenderableSprite(Entity entity, SpriteDefinition spriteDefinition) {
        tempRenderableSprite.clear();
        for (ObjectMap.Entry<String, Object> property : spriteDefinition.getProperties()) {
            tempRenderableSprite.setValue(property.key, evaluatePropertySystem.evaluateProperty(entity, property.value, Object.class));
        }

        return tempRenderableSprite;
    }

    @EventListener
    public void transformUpdated(TransformUpdated transformUpdated, Entity entity) {
        int entityId = entity.getId();
        if (spriteMap.containsKey(entityId))
            updateSprites(entity.getId());
    }

    @Override
    public boolean evaluatesProperty(Entity entity, EvaluableProperty value) {
        return value instanceof SpritePositionProperty
                || value instanceof SpriteUVProperty;
    }

    @Override
    public Object evaluateValue(Entity entity, EvaluableProperty value) {
        if (value instanceof SpritePositionProperty) {
            SpritePositionProperty spritePositionProperty = (SpritePositionProperty) value;

            Matrix4 transform = transformSystem.getResolvedTransform(entity);

            Matrix4 resultTransform = tempMatrix.set(transform).mul(spritePositionProperty.getTransform());
            Vector3 rightVector = spritePositionProperty.getRightVector();
            Vector3 upVector = spritePositionProperty.getUpVector();

            return VectorUtil.createCenterSpritePosition(1f, 1f, rightVector, upVector, resultTransform);
        } else if (value instanceof SpriteUVProperty) {
            SpriteUVProperty spriteUVProperty = (SpriteUVProperty) value;
            if (spriteUVProperty.isInvertedX()) {
                if (spriteUVProperty.isInvertedY())
                    return uvAttributeInvertedBoth;
                else
                    return uvAttributeInvertedX;
            } else {
                if (spriteUVProperty.isInvertedY())
                    return uvAttributeInvertedY;
                else
                    return uvAttribute;
            }
        }
        return null;
    }

    @Override
    protected void inserted(int entityId) {
        Entity spriteEntity = world.getEntity(entityId);
        newSpriteEntities.add(spriteEntity);
    }

    private void addSprites(int entityId, Entity spriteEntity, SpriteComponent sprite) {
        Array<BatchNameWithSpriteReference> spriteComponentAdapters = new Array<>();
        for (SpriteDefinition spriteDefinition : sprite.getSprites()) {
            String batchName = spriteDefinition.getSpriteBatchName();
            ObjectBatchModel<RenderableSprite, SpriteReference> objectBatchModel = spriteBatchSystem.getSpriteBatchModel(batchName);
            SpriteReference objectReference = addSprite(objectBatchModel, spriteEntity, spriteDefinition);
            spriteComponentAdapters.add(new BatchNameWithSpriteReference(batchName, objectReference));
        }

        spriteMap.put(entityId, spriteComponentAdapters);
    }

    @Override
    protected void removed(int entityId) {
        Array<BatchNameWithSpriteReference> sprites = spriteMap.remove(entityId);
        for (BatchNameWithSpriteReference sprite : sprites) {
            spriteBatchSystem.getSpriteBatchModel(sprite.batchName).removeObject(sprite.objectReference);
        }
    }

    @Override
    protected void processSystem() {
        for (Entity newSpriteEntity : newSpriteEntities) {
            SpriteComponent sprite = spriteComponentMapper.get(newSpriteEntity);
            if (sprite != null)
                addSprites(newSpriteEntity.getId(), newSpriteEntity, sprite);
        }
        newSpriteEntities.clear();
    }

    @Override
    public void dispose() {
        spriteMap.clear();
    }

    public static class BatchNameWithSpriteReference {
        private String batchName;
        private SpriteReference objectReference;

        public BatchNameWithSpriteReference(String batchName, SpriteReference objectReference) {
            this.batchName = batchName;
            this.objectReference = objectReference;
        }
    }
}

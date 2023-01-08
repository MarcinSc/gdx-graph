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
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

public class SpriteSystem extends BaseEntitySystem implements PropertyEvaluator {
    private final IntMap<Array<BatchNameWithSpriteIndex>> spriteMap = new IntMap<>();
    private final DefaultRenderableSprite tempRenderableSprite = new DefaultRenderableSprite();

    private ComponentMapper<SpriteComponent> spriteComponentMapper;

    private SpriteBatchSystem spriteBatchSystem;
    private TransformSystem transformSystem;
    private EvaluatePropertySystem evaluatePropertySystem;

    public static final Vector2ValuePerVertex uvAttribute = new Vector2ValuePerVertex(new float[]{0, 0, 1, 0, 0, 1, 1, 1});

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

        for (BatchNameWithSpriteIndex batchNameWithSpriteIndex : spriteMap.remove(entityId)) {
            spriteBatchSystem.getSpriteBatchModel(batchNameWithSpriteIndex.batchName).removeSprite(batchNameWithSpriteIndex.spriteIndex);
        }

        addSprites(entityId, spriteEntity, sprite);
    }

    public void updateSprite(int entityId, int spriteDefinitionIndex) {
        Entity spriteEntity = world.getEntity(entityId);
        SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
        Array<BatchNameWithSpriteIndex> sprites = spriteMap.get(entityId);
        if (sprites != null) {
            BatchNameWithSpriteIndex batchNameWithSpriteIndex = sprites.get(spriteDefinitionIndex);

            int newSpriteIndex = updateSprite(spriteBatchSystem.getSpriteBatchModel(batchNameWithSpriteIndex.batchName),
                    spriteEntity, sprite.getSprites().get(spriteDefinitionIndex),
                    batchNameWithSpriteIndex.spriteIndex);
            batchNameWithSpriteIndex.spriteIndex = newSpriteIndex;
        }
    }

    private int addSprite(SpriteBatchModel spriteBatchModel, Entity entity, SpriteDefinition spriteDefinition) {
        RenderableSprite renderableSprite = obtainRenderableSprite(entity, spriteDefinition);
        return spriteBatchModel.addSprite(renderableSprite);
    }

    private int updateSprite(SpriteBatchModel spriteBatchModel, Entity entity, SpriteDefinition spriteDefinition, int spriteIndex) {
        RenderableSprite renderableSprite = obtainRenderableSprite(entity, spriteDefinition);
        return spriteBatchModel.updateSprite(renderableSprite, spriteIndex);
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
        } else {
            return uvAttribute;
        }
    }

    @Override
    protected void inserted(int entityId) {
        Entity spriteEntity = world.getEntity(entityId);
        newSpriteEntities.add(spriteEntity);
    }

    private void addSprites(int entityId, Entity spriteEntity, SpriteComponent sprite) {
        Array<BatchNameWithSpriteIndex> spriteComponentAdapters = new Array<>();
        for (SpriteDefinition spriteDefinition : sprite.getSprites()) {
            String batchName = spriteDefinition.getSpriteBatchName();
            SpriteBatchModel spriteBatchModel = spriteBatchSystem.getSpriteBatchModel(batchName);
            int spriteIdentifier = addSprite(spriteBatchModel, spriteEntity, spriteDefinition);
            spriteComponentAdapters.add(new BatchNameWithSpriteIndex(batchName, spriteIdentifier));
        }

        spriteMap.put(entityId, spriteComponentAdapters);
    }

    @Override
    protected void removed(int entityId) {
        Array<BatchNameWithSpriteIndex> sprites = spriteMap.remove(entityId);
        for (BatchNameWithSpriteIndex sprite : sprites) {
            spriteBatchSystem.getSpriteBatchModel(sprite.batchName).removeSprite(sprite.spriteIndex);
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
        for (Array<BatchNameWithSpriteIndex> spriteArray : spriteMap.values()) {
            for (BatchNameWithSpriteIndex sprite : spriteArray) {
                spriteBatchSystem.getSpriteBatchModel(sprite.batchName).removeSprite(sprite.spriteIndex);
            }
        }
        spriteMap.clear();
    }

    public static class BatchNameWithSpriteIndex {
        private String batchName;
        private int spriteIndex;

        public BatchNameWithSpriteIndex(String batchName, int spriteIndex) {
            this.batchName = batchName;
            this.spriteIndex = spriteIndex;
        }
    }
}

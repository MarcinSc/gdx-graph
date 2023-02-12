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
import com.gempukku.libgdx.graph.util.storage.MultiPartBatchModel;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformUpdated;

public class SpriteSystem extends BaseEntitySystem implements PropertyEvaluator {
    private final IntMap<ObjectMap<String, SpriteReference>> spriteMap = new IntMap<>();
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
    private final Vector3 tempVector3 = new Vector3();

    private Array<Entity> newSpriteEntities = new Array<>();

    public SpriteSystem() {
        super(Aspect.all(SpriteComponent.class));
    }

    @Override
    protected void initialize() {
        evaluatePropertySystem.addPropertyEvaluator(this);
    }

    public void updateSprite(int entityId) {
        Entity spriteEntity = world.getEntity(entityId);
        SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);

        ObjectMap<String, SpriteReference> oldSprites = spriteMap.get(entityId);
        if (!hasSameSprites(oldSprites, sprite.getSpriteBatchName())) {
            removeSprite(entityId);
            addSprite(entityId, spriteEntity, sprite);
        } else {
            RenderableSprite renderableSprite = obtainRenderableSprite(spriteEntity, sprite);
            for (ObjectMap.Entry<String, SpriteReference> oldSprite : oldSprites) {
                SpriteReference newReference = spriteBatchSystem.getSpriteBatchModel(oldSprite.key).updatePart(renderableSprite, oldSprite.value);
                oldSprites.put(oldSprite.key, newReference);
            }
        }
    }

    private boolean hasSameSprites(ObjectMap<String, SpriteReference> oldSprites, Array<String> spriteNames) {
        if (oldSprites.size != spriteNames.size)
            return false;
        for (String spriteName : spriteNames) {
            if (!oldSprites.containsKey(spriteName))
                return false;
        }
        return true;
    }

    private RenderableSprite obtainRenderableSprite(Entity entity, SpriteComponent sprite) {
        tempRenderableSprite.clear();
        for (ObjectMap.Entry<String, Object> property : sprite.getProperties()) {
            tempRenderableSprite.setValue(property.key, evaluatePropertySystem.evaluateProperty(entity, property.value, Object.class));
        }

        return tempRenderableSprite;
    }

    @EventListener
    public void transformUpdated(TransformUpdated transformUpdated, Entity entity) {
        int entityId = entity.getId();
        if (spriteMap.containsKey(entityId))
            updateSprite(entity.getId());
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

            Matrix4 resultTransform = tempMatrix.set(transform);
            Vector3 rightVector = spritePositionProperty.getRightVector();
            Vector3 upVector = spritePositionProperty.getUpVector();
            Vector3 zVector = tempVector3.set(rightVector).crs(upVector).nor().scl(spritePositionProperty.getZDistance());
            resultTransform.translate(zVector);

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

    private void addSprite(int entityId, Entity spriteEntity, SpriteComponent sprite) {
        ObjectMap<String, SpriteReference> spriteReferences = new ObjectMap<>();
        RenderableSprite renderableSprite = obtainRenderableSprite(spriteEntity, sprite);
        for (String batchName : sprite.getSpriteBatchName()) {
            MultiPartBatchModel<RenderableSprite, SpriteReference> spriteBatchModel = spriteBatchSystem.getSpriteBatchModel(batchName);
            spriteReferences.put(batchName, spriteBatchModel.addPart(renderableSprite));
        }
        spriteMap.put(entityId, spriteReferences);
    }

    @Override
    protected void removed(int entityId) {
        removeSprite(entityId);
    }

    private void removeSprite(int entityId) {
        ObjectMap<String, SpriteReference> sprites = spriteMap.remove(entityId);
        for (ObjectMap.Entry<String, SpriteReference> sprite : sprites) {
            spriteBatchSystem.getSpriteBatchModel(sprite.key).removePart(sprite.value);
        }
    }

    @Override
    protected void processSystem() {
        for (Entity newSpriteEntity : newSpriteEntities) {
            SpriteComponent sprite = spriteComponentMapper.get(newSpriteEntity);
            if (sprite != null)
                addSprite(newSpriteEntity.getId(), newSpriteEntity, sprite);
        }
        newSpriteEntities.clear();
    }

    @Override
    public void dispose() {
        spriteMap.clear();
    }
}

package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class SpriteAdvanceFocus implements WeightedCameraFocus {
    private static final Vector3 tmpVector3 = new Vector3();
    private final Entity entity;
    private final float advanceDistance;
    private final float weight;
    private final float x;
    private final float y;

    public SpriteAdvanceFocus(Entity entity, float advanceDistance) {
        this(entity, advanceDistance, 1f);
    }

    public SpriteAdvanceFocus(Entity entity, float advanceDistance, float weight) {
        this(entity, advanceDistance, weight, 0, 0);
    }

    public SpriteAdvanceFocus(Entity entity, float advanceDistance, float weight, float x, float y) {
        this.entity = entity;
        this.advanceDistance = advanceDistance;
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        FacingComponent facing = entity.getComponent(FacingComponent.class);

        Vector3 spritePosition = position.getPosition(tmpVector3);
        SpriteFaceDirection faceDirection = facing.getFaceDirection();
        return focus.set(spritePosition.x + faceDirection.getX() * advanceDistance + x, spritePosition.y + faceDirection.getY() * advanceDistance + y);
    }
}

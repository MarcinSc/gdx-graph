package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.component.PositionComponent;

public class SpriteFocus implements WeightedCameraFocus {
    private static final Vector3 tmpVector3 = new Vector3();
    public Entity entity;
    private final float weight;
    private final float x;
    private final float y;

    public SpriteFocus(Entity entity) {
        this(entity, 1f);
    }

    public SpriteFocus(Entity entity, float weight) {
        this(entity, weight, 0, 0);
    }

    public SpriteFocus(Entity entity, float weight, float x, float y) {
        this.entity = entity;
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        Vector3 spritePosition = position.getPosition(tmpVector3);
        return focus.set(spritePosition.x + x, spritePosition.y + y);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}

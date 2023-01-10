package com.gempukku.libgdx.graph.test.system.camera;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.camera2d.focus.Camera2DFocus;
import com.gempukku.libgdx.graph.test.component.PlayerInputControlledComponent;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class PlayerAdvanceFocus implements Camera2DFocus {
    private static final Vector3 tmpVector3 = new Vector3();

    private TransformSystem transformSystem;
    private Entity entity;
    private float advance;
    private float weight;

    public PlayerAdvanceFocus(TransformSystem transformSystem, Entity entity, float advance) {
        this(transformSystem, entity, advance, 1f);
    }

    public PlayerAdvanceFocus(TransformSystem transformSystem, Entity entity, float advance, float weight) {
        this.transformSystem = transformSystem;
        this.entity = entity;
        this.advance = advance;
        this.weight = weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        Vector3 position = transformSystem.getResolvedTransform(entity).getTranslation(tmpVector3);
        boolean facingRight = entity.getComponent(PlayerInputControlledComponent.class).isCurrentlyFacingRight();
        return focus.set(position.x + (facingRight ? advance : -advance), position.y);
    }
}

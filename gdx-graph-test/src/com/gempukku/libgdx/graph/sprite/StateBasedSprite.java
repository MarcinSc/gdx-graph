package com.gempukku.libgdx.graph.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class StateBasedSprite implements Sprite, PropertyContainer {
    private static final Vector3 tmpVec3 = new Vector3();
    private static final Vector2 tmpVec2 = new Vector2();
    private String state;
    private final ObjectMap<String, SpriteStateData> statesData;
    private boolean dirty = true;
    private final Entity entity;
    private float animationStart;
    private float outline;

    public StateBasedSprite(Entity entity, String state, ObjectMap<String, SpriteStateData> statesData) {
        this.entity = entity;
        this.state = state;
        this.statesData = statesData;
    }

    public void setState(String state) {
        if (!statesData.containsKey(state))
            throw new IllegalArgumentException("Undefined state for the sprite");
        if (!this.state.equals(state)) {
            dirty = true;
            this.state = state;
        }
    }


    @Override
    public void setOutline(float outline) {
        if (this.outline != outline) {
            this.outline = outline;
            dirty = true;
        }
    }

    @Override
    public Vector3 getPosition() {
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        return positionComponent.getPosition(StateBasedSprite.tmpVec3);
    }

    @Override
    public boolean isRendered(Camera camera) {
        return true;
    }

    @Override
    public PropertyContainer getPropertyContainer(String tag) {
        return this;
    }

    @Override
    public Object getValue(String name) {
        if (name.equals("Size")) {
            final SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
            final FacingComponent facingComponent = entity.getComponent(FacingComponent.class);
            SpriteFaceDirection faceDirection = facingComponent.getFaceDirection();
            return sizeComponent.getSize(StateBasedSprite.tmpVec2).scl(faceDirection.getX(), 1);
        }
        if (name.equals("Anchor")) {
            final AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);
            return anchorComponent.getAnchor(StateBasedSprite.tmpVec2);
        }
        if (name.equals("Texture")) {
            SpriteStateData spriteStateData = statesData.get(state);
            return spriteStateData.sprites;
        }
        if (name.equals("Outline")) {
            return outline;
        }
        if (name.equals("Animation Start")) {
            return animationStart;
        }
        if (name.equals("Animation Speed")) {
            SpriteStateData spriteStateData = statesData.get(state);
            return spriteStateData.speed;
        }
        if (name.equals("Animation Looping")) {
            SpriteStateData spriteStateData = statesData.get(state);
            return spriteStateData.looping ? 1f : 0f;
        }
        if (name.equals("Sprite Count")) {
            SpriteStateData spriteStateData = statesData.get(state);
            StateBasedSprite.tmpVec2.set(spriteStateData.spriteWidth, spriteStateData.spriteHeight);
            return StateBasedSprite.tmpVec2;
        }
        return null;
    }

    @Override
    public boolean cleanup(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        final FacingComponent facingComponent = entity.getComponent(FacingComponent.class);
        boolean attributeDirty = positionComponent.isDirty() || facingComponent.isDirty();

        boolean result = attributeDirty || dirty;

        if (attributeDirty) {
            positionComponent.clean();
            facingComponent.clean();
        }

        if (dirty) {
            animationStart = timeProvider.getTime();
        }

        dirty = false;
        return result;
    }
}

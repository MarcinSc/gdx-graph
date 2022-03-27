package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PhysicsComponent;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.graph.sprite.StateBasedSprite;
import com.gempukku.libgdx.graph.system.sensor.FootSensorData;

public class PlayerControlSystem extends EntitySystem {
    private Entity playerEntity;

    public PlayerControlSystem(int priority) {
        super(priority);
    }

    public void setPlayerEntity(Entity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public void update(float delta) {
        PhysicsComponent physicsComponent = playerEntity.getComponent(PhysicsComponent.class);
        SpriteComponent spriteComponent = playerEntity.getComponent(SpriteComponent.class);
        FacingComponent facingComponent = playerEntity.getComponent(FacingComponent.class);

        FootSensorData footSensorData = (FootSensorData) physicsComponent.getSensorDataOfType("foot").getValue();
        boolean grounded = (footSensorData != null) && footSensorData.isGrounded();

        Body playerBody = physicsComponent.getBody();
        StateBasedSprite playerSprite = (StateBasedSprite) spriteComponent.getSprite();
        float desiredHorizontalVelocity = getDesiredHorizontalVelocity();

        float verticalVelocity = playerBody.getLinearVelocity().y;
        playerBody.setLinearVelocity(desiredHorizontalVelocity, verticalVelocity);

        if (desiredHorizontalVelocity > 0) {
            facingComponent.setFaceDirection(SpriteFaceDirection.Right);
        } else if (desiredHorizontalVelocity < 0) {
            facingComponent.setFaceDirection(SpriteFaceDirection.Left);
        }
        if (desiredHorizontalVelocity != 0 && grounded)
            playerSprite.setState("Walk");
        else if (verticalVelocity == 0)
            playerSprite.setState("Idle");

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && footSensorData != null && grounded) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, 14);
            playerSprite.setState("Jump");
        }
    }

    private float getDesiredHorizontalVelocity() {
        if (isRightPressed() && !isLeftPressed())
            return 5;
        else if (isLeftPressed() && !isRightPressed())
            return -5;
        else
            return 0;
    }

    private boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    private boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }
}

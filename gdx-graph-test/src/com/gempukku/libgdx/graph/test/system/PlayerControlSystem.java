package com.gempukku.libgdx.graph.test.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.box2d.artemis.PhysicsComponent;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteComponent;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.sprite.property.SpriteUVProperty;
import com.gempukku.libgdx.graph.test.component.PlayerInputControlledComponent;
import com.gempukku.libgdx.graph.test.component.StateBasedSpriteComponent;
import com.gempukku.libgdx.graph.test.system.sensor.FootSensorData;
import com.gempukku.libgdx.lib.artemis.input.UserInputStateComponent;

public class PlayerControlSystem extends EntitySystem {
    private SpriteSystem spriteSystem;
    private PipelineRendererSystem pipelineRendererSystem;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerInputControlledComponent.class));
    }

    @Override
    protected void processSystem() {
        Bag<Entity> playerControlledEntities = getEntities();
        for (int i = 0, size = playerControlledEntities.size(); i < size; i++) {
            Entity playerControlledEntity = playerControlledEntities.get(i);

            PlayerInputControlledComponent playerInputControlled = playerControlledEntity.getComponent(PlayerInputControlledComponent.class);
            PhysicsComponent physicsComponent = playerControlledEntity.getComponent(PhysicsComponent.class);
            UserInputStateComponent userInputState = playerControlledEntity.getComponent(UserInputStateComponent.class);

            FootSensorData footSensorData = (FootSensorData) physicsComponent.getSensorDataOfType("foot").getValue();
            boolean grounded = (footSensorData != null) && footSensorData.isGrounded();

            Body playerBody = physicsComponent.getBody();

            float desiredHorizontalVelocity = getDesiredHorizontalVelocity(playerInputControlled, userInputState);

            float verticalVelocity = playerBody.getLinearVelocity().y;
            playerBody.setLinearVelocity(desiredHorizontalVelocity, verticalVelocity);

            if (desiredHorizontalVelocity > 0) {
                setFaceRight(playerControlledEntity, playerInputControlled, true);
            } else if (desiredHorizontalVelocity < 0) {
                setFaceRight(playerControlledEntity, playerInputControlled, false);
            }

            StateBasedSpriteComponent stateBasedSprite = playerControlledEntity.getComponent(StateBasedSpriteComponent.class);
            if (desiredHorizontalVelocity != 0 && grounded) {
                updateSpriteState(playerControlledEntity, playerInputControlled, stateBasedSprite, "Walk");
            } else if (verticalVelocity == 0) {
                updateSpriteState(playerControlledEntity, playerInputControlled, stateBasedSprite, "Idle");
            }

            if (userInputState.getSignals().contains("jump") && footSensorData != null && grounded) {
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, playerInputControlled.getJumpVelocity());
            }

            if (verticalVelocity != 0 && !grounded) {
                updateSpriteState(playerControlledEntity, playerInputControlled, stateBasedSprite, "Jump");
            }
        }
    }

    private void setFaceRight(Entity playerEntity, PlayerInputControlledComponent playerInputControlled, boolean faceRight) {
        if (playerInputControlled.isCurrentlyFacingRight() != faceRight) {
            ObjectMap<String, Object> properties = playerEntity.getComponent(SpriteComponent.class).getSprites().get(0).getProperties();
            SpriteUVProperty spriteUVProperty = (SpriteUVProperty) properties.get("UV");
            spriteUVProperty.setInvertedX(!faceRight);
            spriteSystem.updateSprite(playerEntity.getId(), 0);

            playerInputControlled.setCurrentlyFacingRight(faceRight);
        }
    }

    private void updateSpriteState(Entity playerEntity, PlayerInputControlledComponent playerInputControlled, StateBasedSpriteComponent stateBasedSprite, String state) {
        if (!state.equals(playerInputControlled.getCurrentState())) {
            ObjectMap<String, Object> properties = playerEntity.getComponent(SpriteComponent.class).getSprites().get(0).getProperties();
            ObjectMap<String, Object> stateProperties = stateBasedSprite.getStateProperties().get(state);
            for (ObjectMap.Entry<String, Object> stateProperty : stateProperties) {
                properties.put(stateProperty.key, stateProperty.value);
            }
            properties.put("Animation Start", pipelineRendererSystem.getCurrentTime());
            spriteSystem.updateSprite(playerEntity.getId(), 0);

            playerInputControlled.setCurrentState(state);
        }
    }

    private float getDesiredHorizontalVelocity(PlayerInputControlledComponent playerInputControlled, UserInputStateComponent userInputState) {
        if (isRightPressed(userInputState) && !isLeftPressed(userInputState))
            return playerInputControlled.getRunVelocity();
        else if (isLeftPressed(userInputState) && !isRightPressed(userInputState))
            return -playerInputControlled.getRunVelocity();
        else
            return 0;
    }

    private boolean isRightPressed(UserInputStateComponent userInputState) {
        return userInputState.getStates().contains("right");
    }

    private boolean isLeftPressed(UserInputStateComponent userInputState) {
        return userInputState.getStates().contains("left");
    }
}

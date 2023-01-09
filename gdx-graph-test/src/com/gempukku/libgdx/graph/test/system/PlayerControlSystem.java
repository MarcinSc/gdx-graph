package com.gempukku.libgdx.graph.test.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.box2d.artemis.PhysicsComponent;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteComponent;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.artemis.sprite.property.SpriteUVProperty;
import com.gempukku.libgdx.graph.test.component.StateBasedSpriteComponent;
import com.gempukku.libgdx.graph.test.system.sensor.FootSensorData;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class PlayerControlSystem extends BaseSystem {
    private SpriteSystem spriteSystem;
    private TransformSystem transformSystem;
    private PipelineRendererSystem pipelineRendererSystem;

    private Entity playerEntity;

    private String currentState = "Idle";
    private boolean currentlyFacingRight = true;

    private Matrix4 tmpMatrix4 = new Matrix4();

    public void setPlayerEntity(Entity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    protected void processSystem() {
        if (playerEntity != null) {
            PhysicsComponent physicsComponent = playerEntity.getComponent(PhysicsComponent.class);

            FootSensorData footSensorData = (FootSensorData) physicsComponent.getSensorDataOfType("foot").getValue();
            boolean grounded = (footSensorData != null) && footSensorData.isGrounded();

            Body playerBody = physicsComponent.getBody();

            float desiredHorizontalVelocity = getDesiredHorizontalVelocity();

            float verticalVelocity = playerBody.getLinearVelocity().y;
            playerBody.setLinearVelocity(desiredHorizontalVelocity, verticalVelocity);

            if (desiredHorizontalVelocity > 0) {
                setFaceRight(true);
            } else if (desiredHorizontalVelocity < 0) {
                setFaceRight(false);
            }

            StateBasedSpriteComponent stateBasedSprite = playerEntity.getComponent(StateBasedSpriteComponent.class);
            if (desiredHorizontalVelocity != 0 && grounded) {
                updateSpriteState(stateBasedSprite, "Walk");
            } else if (verticalVelocity == 0) {
                updateSpriteState(stateBasedSprite, "Idle");
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && footSensorData != null && grounded) {
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, 14);
            }

            if (verticalVelocity != 0 && !grounded) {
                updateSpriteState(stateBasedSprite, "Jump");
            }
        }
    }

    private void setFaceRight(boolean faceRight) {
        if (currentlyFacingRight != faceRight) {
            ObjectMap<String, Object> properties = playerEntity.getComponent(SpriteComponent.class).getSprites().get(0).getProperties();
            SpriteUVProperty spriteUVProperty = (SpriteUVProperty) properties.get("UV");
            spriteUVProperty.setInvertedX(!faceRight);
            spriteSystem.updateSprite(playerEntity.getId(), 0);

            currentlyFacingRight = faceRight;
        }
    }

    private void updateSpriteState(StateBasedSpriteComponent stateBasedSprite, String state) {
        if (!state.equals(currentState)) {
            ObjectMap<String, Object> properties = playerEntity.getComponent(SpriteComponent.class).getSprites().get(0).getProperties();
            ObjectMap<String, Object> stateProperties = stateBasedSprite.getStateProperties().get(state);
            for (ObjectMap.Entry<String, Object> stateProperty : stateProperties) {
                properties.put(stateProperty.key, stateProperty.value);
            }
            properties.put("Animation Start", pipelineRendererSystem.getCurrentTime());
            spriteSystem.updateSprite(playerEntity.getId(), 0);

            currentState = state;
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

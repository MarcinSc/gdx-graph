package com.gempukku.libgdx.graph.test.system.sensor;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorContactListener;
import com.gempukku.libgdx.box2d.artemis.sensor.SensorData;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteComponent;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteDefinition;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;

public class InteractSensorContactListener implements SensorContactListener {
    private SpriteSystem spriteSystem;

    public InteractSensorContactListener(SpriteSystem spriteSystem) {
        this.spriteSystem = spriteSystem;
    }

    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        SpriteDefinition interactSprite = entity.getComponent(SpriteComponent.class).getSprites().get(1);
        interactSprite.getProperties().put("Outline Width", 3f);

        spriteSystem.updateSprite(entity.getId(), 1);
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        SpriteDefinition interactSprite = entity.getComponent(SpriteComponent.class).getSprites().get(1);
        interactSprite.getProperties().put("Outline Width", 0f);

        spriteSystem.updateSprite(entity.getId(), 1);
    }
}

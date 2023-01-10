package com.gempukku.libgdx.graph.test.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteComponent;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteDefinition;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;

public class OutlineSystem extends BaseSystem {
    private SpriteSystem spriteSystem;

    public void setOutline(Entity entity, boolean outline) {
        String spriteBatchName = entity.getComponent(SpriteComponent.class).getSprites().get(0).getSpriteBatchName();
        if (spriteBatchName.equals("animatedSprite")) {
            SpriteDefinition interactSprite = entity.getComponent(SpriteComponent.class).getSprites().get(1);
            interactSprite.getProperties().put("Outline Width", outline ? 3f : 0f);

            spriteSystem.updateSprite(entity.getId(), 1);
        } else {
            SpriteDefinition interactSprite = entity.getComponent(SpriteComponent.class).getSprites().get(0);
            interactSprite.getProperties().put("Outline", outline ? 1f : 0f);

            spriteSystem.updateSprite(entity.getId(), 0);
        }
    }

    @Override
    protected void processSystem() {

    }
}

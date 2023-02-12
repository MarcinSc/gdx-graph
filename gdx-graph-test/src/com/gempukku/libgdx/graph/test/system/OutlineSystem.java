package com.gempukku.libgdx.graph.test.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteComponent;
import com.gempukku.libgdx.graph.artemis.sprite.SpriteSystem;
import com.gempukku.libgdx.graph.test.component.OutlineComponent;

public class OutlineSystem extends BaseSystem {
    private SpriteSystem spriteSystem;

    public void setOutline(Entity entity, boolean outline) {
        OutlineComponent outlineComp = entity.getComponent(OutlineComponent.class);
        SpriteComponent interactSprite = entity.getComponent(SpriteComponent.class);
        if (outlineComp.getType().equals("asSprite")) {
            if (outline && !interactSprite.getSpriteBatchName().contains("spriteOutline", false)) {
                interactSprite.getSpriteBatchName().add("spriteOutline");
                spriteSystem.updateSprite(entity.getId());
            } else if (!outline && interactSprite.getSpriteBatchName().contains("spriteOutline", false)) {
                interactSprite.getSpriteBatchName().removeValue("spriteOutline", false);
                spriteSystem.updateSprite(entity.getId());
            }
        } else {
            interactSprite.getProperties().put("Outline", outline ? 1f : 0f);
            spriteSystem.updateSprite(entity.getId());
        }
    }

    @Override
    protected void processSystem() {

    }
}

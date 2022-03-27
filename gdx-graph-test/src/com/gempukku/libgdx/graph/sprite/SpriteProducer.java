package com.gempukku.libgdx.graph.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.entity.def.SimpleSpriteDef;
import com.gempukku.libgdx.graph.entity.def.SpriteStateDataDef;
import com.gempukku.libgdx.graph.entity.def.StateBasedSpriteDef;
import com.gempukku.libgdx.graph.entity.def.TiledSpriteDef;

public class SpriteProducer {
    public static Sprite createSprite(Entity entity, TextureLoader textureLoader, SpriteComponent spriteComponent) {
        String spriteType = spriteComponent.getSpriteType();
        switch (spriteType) {
            case "stateBased":
                return createStateBasedSprite(entity, textureLoader, spriteComponent.getStateBasedSprite());
            case "tiled":
                return createTiledSprite(entity, textureLoader, spriteComponent.getTiledSprite());
            case "simple":
                return createSimpleSprite(entity, textureLoader, spriteComponent.getSimpleSprite());
        }
        throw new IllegalArgumentException("Unknown type of sprite");
    }

    private static Sprite createSimpleSprite(Entity entity, TextureLoader textureLoader, SimpleSpriteDef simpleSprite) {
        return new SimpleSprite(entity, new TextureRegion(textureLoader.loadTexture(simpleSprite.getTexture()), simpleSprite.getU(), simpleSprite.getV(),
                simpleSprite.getU2(), simpleSprite.getV2()));
    }

    public static Sprite createStateBasedSprite(Entity entity, TextureLoader textureLoader, StateBasedSpriteDef stateBasedSpriteDef) {
        ObjectMap<String, SpriteStateData> stateData = new ObjectMap<>();
        for (ObjectMap.Entry<String, SpriteStateDataDef> stateEntry : stateBasedSpriteDef.getStateData().entries()) {
            String key = stateEntry.key;
            SpriteStateDataDef def = stateEntry.value;
            SpriteStateData data = new SpriteStateData(
                    new TextureRegion(textureLoader.loadTexture(def.getTexture()), def.getU(), def.getV(), def.getU2(), def.getV2()),
                    def.getWidth(), def.getHeight(), def.getSpeed(), def.isLooping());
            stateData.put(key, data);
        }

        return new StateBasedSprite(entity, stateBasedSpriteDef.getState(), stateData);
    }

    private static Sprite createTiledSprite(Entity entity, TextureLoader textureLoader, TiledSpriteDef tiledSpriteDef) {
        return new TiledSprite(entity,
                new TextureRegion(textureLoader.loadTexture(tiledSpriteDef.getTileTexture()), tiledSpriteDef.getU(), tiledSpriteDef.getV(), tiledSpriteDef.getU2(), tiledSpriteDef.getV2()),
                tiledSpriteDef.getTileRepeat());
    }

    public interface TextureLoader {
        Texture loadTexture(String path);
    }
}

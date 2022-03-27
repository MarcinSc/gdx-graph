package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.entity.def.SimpleSpriteDef;
import com.gempukku.libgdx.graph.entity.def.StateBasedSpriteDef;
import com.gempukku.libgdx.graph.entity.def.TiledSpriteDef;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.sprite.Sprite;

public class SpriteComponent implements Component {
    private String[] tags;
    private String spriteType;

    private StateBasedSpriteDef stateBasedSprite;
    private TiledSpriteDef tiledSprite;
    private SimpleSpriteDef simpleSprite;

    private Sprite sprite;
    private ObjectMap<String, GraphSprite> graphSprites;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public ObjectMap<String, GraphSprite> getGraphSprites() {
        return graphSprites;
    }

    public void setGraphSprites(ObjectMap<String, GraphSprite> graphSprites) {
        this.graphSprites = graphSprites;
    }

    public String[] getTags() {
        return tags;
    }

    public String getSpriteType() {
        return spriteType;
    }

    public StateBasedSpriteDef getStateBasedSprite() {
        return stateBasedSprite;
    }

    public TiledSpriteDef getTiledSprite() {
        return tiledSprite;
    }

    public SimpleSpriteDef getSimpleSprite() {
        return simpleSprite;
    }
}

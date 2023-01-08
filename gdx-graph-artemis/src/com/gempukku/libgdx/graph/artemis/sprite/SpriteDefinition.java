package com.gempukku.libgdx.graph.artemis.sprite;

import com.badlogic.gdx.utils.ObjectMap;

public class SpriteDefinition {
    private String spriteBatchName;
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public String getSpriteBatchName() {
        return spriteBatchName;
    }

    public void setSpriteBatchName(String spriteBatchName) {
        this.spriteBatchName = spriteBatchName;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}

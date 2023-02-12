package com.gempukku.libgdx.graph.artemis.sprite;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteComponent extends PooledComponent {
    private Array<String> spriteBatchName = new Array<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public Array<String> getSpriteBatchName() {
        return spriteBatchName;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    @Override
    protected void reset() {
        spriteBatchName.clear();
        properties.clear();
    }
}

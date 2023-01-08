package com.gempukku.libgdx.graph.artemis.sprite;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;

public class SpriteComponent extends PooledComponent {
    private Array<SpriteDefinition> sprites = new Array<>();

    public Array<SpriteDefinition> getSprites() {
        return sprites;
    }

    @Override
    protected void reset() {
        sprites.clear();
    }
}

package com.gempukku.libgdx.graph.plugin.sprites.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteData;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpritesImpl;

public interface SpriteRenderingStrategy {
    boolean isBatched();

    void processSprites(GraphSpritesImpl sprites, Array<String> tags, Camera camera, StrategyCallback callback);

    interface StrategyCallback {
        void begin();

        void process(SpriteData spriteData, String tag);

        void end();
    }
}

package com.gempukku.libgdx.graph.plugin.sprites.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.sprites.impl.BatchedSpriteData;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpritesImpl;

public class ShaderUnorderedSpriteRenderingStrategy implements SpriteRenderingStrategy {
    @Override
    public boolean isBatched() {
        return true;
    }

    @Override
    public void processSprites(GraphSpritesImpl sprites, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            for (Array<BatchedSpriteData> batchedSpriteDatum : sprites.getBatchedSpriteData(tag)) {
                for (BatchedSpriteData spriteData : batchedSpriteDatum) {
                    callback.process(spriteData, tag);
                }
            }
        }
        callback.end();
    }
}

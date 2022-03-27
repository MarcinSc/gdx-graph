package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;

public class GraphSpriteImpl implements GraphSprite {
    private final String tag;
    private final RenderableSprite renderableSprite;

    public GraphSpriteImpl(String tag, RenderableSprite renderableSprite) {
        this.tag = tag;
        this.renderableSprite = renderableSprite;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public RenderableSprite getRenderableSprite() {
        return renderableSprite;
    }
}

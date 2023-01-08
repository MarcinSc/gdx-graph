package com.gempukku.libgdx.graph.artemis.text;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;

public class TextComponent extends PooledComponent {
    private Array<TextBlock> textBlocks = new Array<>();

    public Array<TextBlock> getTextBlocks() {
        return textBlocks;
    }

    @Override
    protected void reset() {
        textBlocks.clear();
    }
}

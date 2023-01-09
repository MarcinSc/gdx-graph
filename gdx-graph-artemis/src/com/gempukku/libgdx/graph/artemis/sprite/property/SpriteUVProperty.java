package com.gempukku.libgdx.graph.artemis.sprite.property;

import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;

public class SpriteUVProperty implements EvaluableProperty {
    private boolean invertedX;
    private boolean invertedY;

    public boolean isInvertedX() {
        return invertedX;
    }

    public void setInvertedX(boolean invertedX) {
        this.invertedX = invertedX;
    }

    public boolean isInvertedY() {
        return invertedY;
    }

    public void setInvertedY(boolean invertedY) {
        this.invertedY = invertedY;
    }
}

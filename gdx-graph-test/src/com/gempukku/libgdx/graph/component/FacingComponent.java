package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class FacingComponent implements Component {
    private SpriteFaceDirection faceDirection;
    private boolean dirty;

    public SpriteFaceDirection getFaceDirection() {
        return faceDirection;
    }

    public void setFaceDirection(SpriteFaceDirection faceDirection) {
        this.faceDirection = faceDirection;
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        dirty = false;
    }
}

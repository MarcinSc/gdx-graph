package com.gempukku.libgdx.graph.artemis.particle.property;

import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;

public class EntityPositionProperty implements EvaluableProperty {
    private int entityId;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}

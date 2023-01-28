package com.gempukku.libgdx.graph.util.particles;

import com.gempukku.libgdx.graph.util.model.WritableRenderableModel;
import com.gempukku.libgdx.graph.util.storage.DefaultMultiPartRenderableModel;
import com.gempukku.libgdx.graph.util.storage.MultiPartMesh;

public class ParticleMultiPartRenderableModelGdx<T, U> extends DefaultMultiPartRenderableModel<T, U> {
    private float maxDeathTime = Float.MIN_VALUE;

    public ParticleMultiPartRenderableModelGdx(MultiPartMesh<T, U> multiPartMemoryMesh, WritableRenderableModel renderableModel) {
        super(multiPartMemoryMesh, renderableModel);
    }

    public void updateWithMaxDeathTime(float maxDeathTime) {
        this.maxDeathTime = Math.max(this.maxDeathTime, maxDeathTime);
    }

    public float getMaxDeathTime() {
        return maxDeathTime;
    }
}

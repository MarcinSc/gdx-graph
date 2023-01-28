package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.manager.LimitedCapacityObjectRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.ObjectMeshStorage;

public class ParticleObjectRenderableModel<T, U> extends LimitedCapacityObjectRenderableModel<T, U> {
    private float maxDeathTime = Float.MIN_VALUE;

    public ParticleObjectRenderableModel(
            ObjectMeshStorage<T, U> objectMeshStorage, VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer) {
        super(false, objectMeshStorage, vertexAttributes, propertyContainer, new QuadSpriteModel());
    }

    public ParticleObjectRenderableModel(
            ObjectMeshStorage<T, U> objectMeshStorage, VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        super(false, objectMeshStorage, vertexAttributes, propertyContainer, spriteModel);
    }

    public void updateWithMaxDeathTime(float maxDeathTime) {
        this.maxDeathTime = Math.max(this.maxDeathTime, maxDeathTime);
    }

    public float getMaxDeathTime() {
        return maxDeathTime;
    }
}

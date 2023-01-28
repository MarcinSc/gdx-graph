package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.renderer.TrianglesMeshRenderer;
import com.gempukku.libgdx.graph.util.storage.GdxMeshRenderableModel;
import com.gempukku.libgdx.graph.util.storage.ObjectMeshStorage;

public class ParticleObjectRenderableModelGdx<T, U> extends GdxMeshRenderableModel<T, U> {
    private float maxDeathTime = Float.MIN_VALUE;

    public ParticleObjectRenderableModelGdx(
            ObjectMeshStorage<T, U> objectMeshStorage, VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer) {
        this(objectMeshStorage, vertexAttributes, propertyContainer, new TrianglesMeshRenderer());
    }

    public ParticleObjectRenderableModelGdx(
            ObjectMeshStorage<T, U> objectMeshStorage, VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, MeshRenderer meshRenderer) {
        super(false, objectMeshStorage, vertexAttributes, propertyContainer, meshRenderer);
    }

    public void updateWithMaxDeathTime(float maxDeathTime) {
        this.maxDeathTime = Math.max(this.maxDeathTime, maxDeathTime);
    }

    public float getMaxDeathTime() {
        return maxDeathTime;
    }
}

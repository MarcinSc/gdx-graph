package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.sprite.manager.LimitedCapacitySpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class ParticleSpriteRenderableModel extends LimitedCapacitySpriteRenderableModel {
    private float maxDeathTime = Float.MIN_VALUE;

    public ParticleSpriteRenderableModel(boolean staticBatch, int spriteCapacity,
                                         VertexAttributes vertexAttributes,
                                         ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
                                         WritablePropertyContainer propertyContainer,
                                         SpriteModel spriteModel) {
        super(staticBatch, spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer, spriteModel);
    }

    public void updateWithMaxDeathTime(float maxDeathTime) {
        this.maxDeathTime = Math.max(this.maxDeathTime, maxDeathTime);
    }

    public float getMaxDeathTime() {
        return maxDeathTime;
    }
}

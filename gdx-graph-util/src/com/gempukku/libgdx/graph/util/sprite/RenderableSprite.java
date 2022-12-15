package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableSprite {
    /**
     * Should return properties of the sprite.
     *
     * @return
     */
    PropertyContainer getPropertyContainer();

    void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex);
}

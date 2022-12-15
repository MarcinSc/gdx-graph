package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableSprite extends PropertyContainer {
    void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex);
}

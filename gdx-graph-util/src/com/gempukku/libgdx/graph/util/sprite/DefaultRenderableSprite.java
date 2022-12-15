package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;

public class DefaultRenderableSprite implements RenderableSprite {
    private final WritablePropertyContainer propertyContainer;

    public DefaultRenderableSprite(WritablePropertyContainer propertyContainer) {
        this.propertyContainer = propertyContainer;
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setUnknownPropertyInAttribute(VertexAttribute vertexAttribute, float[] vertexData, int startIndex) {

    }
}

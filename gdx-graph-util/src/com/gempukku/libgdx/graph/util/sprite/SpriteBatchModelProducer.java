package com.gempukku.libgdx.graph.util.sprite;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;

public interface SpriteBatchModelProducer {
    SpriteBatchModel create(WritablePropertyContainer propertyContainer);
}

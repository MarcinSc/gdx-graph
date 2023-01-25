package com.gempukku.libgdx.graph.util.sprite.manager;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;

public interface SpriteBatchModelProducer<T extends SpriteBatchModel> {
    T createModel(WritablePropertyContainer propertyContainer);

    void dispose(T model);
}

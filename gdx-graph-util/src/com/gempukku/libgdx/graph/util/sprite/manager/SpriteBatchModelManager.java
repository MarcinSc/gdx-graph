package com.gempukku.libgdx.graph.util.sprite.manager;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;

public interface SpriteBatchModelManager<T extends SpriteBatchModel> {
    T createNewModel(WritablePropertyContainer propertyContainer);

    boolean shouldDisposeEmptyModel(T model);

    void disposeModel(T model);
}

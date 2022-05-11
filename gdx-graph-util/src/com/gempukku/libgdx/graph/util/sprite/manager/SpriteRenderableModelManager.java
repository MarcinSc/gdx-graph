package com.gempukku.libgdx.graph.util.sprite.manager;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.SpriteRenderableModel;

public interface SpriteRenderableModelManager<T extends SpriteRenderableModel> {
    T createNewModel(WritablePropertyContainer propertyContainer);

    boolean shouldDisposeEmptyModel(T model);

    void disposeModel(T model);
}

package com.gempukku.libgdx.graph.util.sprite.manager;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;

public class MinimumSpriteBatchModelManager<T extends SpriteBatchModel> implements SpriteBatchModelManager<T> {
    private final int preserveMinimum;
    private SpriteBatchModelProducer<T> producer;

    private int modelCount = 0;

    public MinimumSpriteBatchModelManager(int preserveMinimum, SpriteBatchModelProducer<T> producer) {
        this.preserveMinimum = preserveMinimum;
        this.producer = producer;
    }

    @Override
    public T createNewModel(WritablePropertyContainer propertyContainer) {
        modelCount++;
        return producer.createModel(propertyContainer);
    }

    @Override
    public boolean shouldDisposeEmptyModel(T model) {
        return modelCount > preserveMinimum;
    }

    @Override
    public void disposeModel(T model) {
        producer.dispose(model);
        modelCount--;
    }
}

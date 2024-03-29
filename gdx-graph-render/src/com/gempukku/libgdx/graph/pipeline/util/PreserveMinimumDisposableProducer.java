package com.gempukku.libgdx.graph.pipeline.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class PreserveMinimumDisposableProducer<T> implements DisposableProducer<T>, Disposable {
    private final Array<T> preserved = new Array<>();
    private final int preserveCount;
    private final DisposableProducer<T> producer;

    private int currentCount;

    public PreserveMinimumDisposableProducer(int preserveCount, DisposableProducer<T> producer) {
        this.preserveCount = preserveCount;
        this.producer = producer;
    }

    @Override
    public T create() {
        currentCount++;
        if (!preserved.isEmpty()) {
            return preserved.pop();
        } else {
            return producer.create();
        }
    }

    @Override
    public void dispose(T disposable) {
        currentCount--;
        if (currentCount < preserveCount) {
            preserved.add(disposable);
        } else {
            producer.dispose(disposable);
        }
    }

    @Override
    public void dispose() {
        for (T disposable : preserved) {
            producer.dispose(disposable);
        }
        preserved.clear();
    }
}

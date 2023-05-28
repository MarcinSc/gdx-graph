package com.gempukku.libgdx.graph.pipeline.util;

public interface DisposableProducer<T> {
    T create();

    void dispose(T disposable);
}

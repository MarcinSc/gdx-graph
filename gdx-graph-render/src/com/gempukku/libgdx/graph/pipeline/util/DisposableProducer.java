package com.gempukku.libgdx.graph.pipeline.util;

import com.badlogic.gdx.utils.Disposable;

public interface DisposableProducer<T extends Disposable> {
    T create();

    void dispose(T disposable);
}

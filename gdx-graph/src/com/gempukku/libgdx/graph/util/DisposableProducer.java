package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.Disposable;

public interface DisposableProducer<T extends Disposable> extends Disposable {
    T create();

    void dispose(T disposable);
}

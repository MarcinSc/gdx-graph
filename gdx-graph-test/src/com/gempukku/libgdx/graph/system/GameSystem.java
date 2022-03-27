package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.utils.Disposable;

public interface GameSystem extends Disposable {
    void update(float delta);
}

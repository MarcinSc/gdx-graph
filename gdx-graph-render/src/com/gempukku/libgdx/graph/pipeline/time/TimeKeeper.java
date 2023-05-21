package com.gempukku.libgdx.graph.pipeline.time;

public interface TimeKeeper extends TimeProvider {
    void updateTime(float delta);
}

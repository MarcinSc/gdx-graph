package com.gempukku.libgdx.graph.artemis.renderer;

import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public class SimpleTimeProvider implements TimeProvider {
    private float delta;
    private float time;

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public float getTime() {
        return time;
    }

    @Override
    public float getDelta() {
        return delta;
    }
}

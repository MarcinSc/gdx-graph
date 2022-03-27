package com.gempukku.libgdx.graph.util;

import com.gempukku.libgdx.graph.time.TimeKeeper;

public class DefaultTimeKeeper implements TimeKeeper {
    private boolean firstUpdate = true;
    private float timeCumulative = 0;
    private float delta;
    private boolean paused;

    @Override
    public void updateTime(float delta) {
        if (!paused) {
            this.delta = delta;
            if (!firstUpdate)
                timeCumulative += delta;
            firstUpdate = false;
        } else {
            this.delta = 0;
        }
    }

    @Override
    public float getTime() {
        return timeCumulative;
    }

    @Override
    public float getDelta() {
        return delta;
    }

    public void pauseTime() {
        paused = true;
    }

    public void resumeTime() {
        paused = false;
    }

    public void setTime(float time) {
        timeCumulative = time;
        firstUpdate = true;
    }
}

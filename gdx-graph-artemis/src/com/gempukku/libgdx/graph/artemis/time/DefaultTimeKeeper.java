package com.gempukku.libgdx.graph.artemis.time;

import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;

public class DefaultTimeKeeper implements TimeKeeper {
    private float timeCumulative = 0.0f;
    private float delta;
    private boolean paused;
    private float timeMultiplier = 1f;

    public DefaultTimeKeeper() {
    }

    public void updateTime(float delta) {
        if (!this.paused) {
            float timeDiff = timeMultiplier * delta;
            this.delta = timeDiff;
            this.timeCumulative += timeDiff;
        } else {
            this.delta = 0.0f;
        }
    }

    public void setTimeMultiplier(float timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
    }

    public float getTimeMultiplier() {
        return timeMultiplier;
    }

    public boolean isPaused() {
        return paused;
    }

    public float getTime() {
        return this.timeCumulative;
    }

    public float getDelta() {
        return this.delta;
    }

    public void pauseTime() {
        this.paused = true;
    }

    public void resumeTime() {
        this.paused = false;
    }

    public void setTime(float time) {
        this.timeCumulative = time;
    }
}

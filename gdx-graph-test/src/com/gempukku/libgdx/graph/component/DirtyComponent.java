package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;

public abstract class DirtyComponent implements Component {
    private boolean dirty = true;

    protected void setDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        dirty = false;
    }
}

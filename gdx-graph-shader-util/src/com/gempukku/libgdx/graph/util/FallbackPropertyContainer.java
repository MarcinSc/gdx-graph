package com.gempukku.libgdx.graph.util;

import com.gempukku.libgdx.graph.data.PropertyContainer;

public class FallbackPropertyContainer implements PropertyContainer {
    private PropertyContainer main;
    private PropertyContainer fallback;

    public void setMain(PropertyContainer main) {
        this.main = main;
    }

    public void setFallback(PropertyContainer fallback) {
        this.fallback = fallback;
    }

    @Override
    public Object getValue(String name) {
        Object value = main.getValue(name);
        if (value != null)
            return value;
        return fallback.getValue(name);
    }
}

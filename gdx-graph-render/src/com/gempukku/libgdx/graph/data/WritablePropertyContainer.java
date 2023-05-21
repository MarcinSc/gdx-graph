package com.gempukku.libgdx.graph.data;

public interface WritablePropertyContainer extends PropertyContainer {
    void setValue(String name, Object value);

    void remove(String name);
}

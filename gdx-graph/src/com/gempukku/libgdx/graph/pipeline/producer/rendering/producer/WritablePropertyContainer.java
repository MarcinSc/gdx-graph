package com.gempukku.libgdx.graph.pipeline.producer.rendering.producer;

public interface WritablePropertyContainer extends PropertyContainer {
    void setValue(String name, Object value);

    void remove(String name);

    void clear();
}

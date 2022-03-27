package com.gempukku.libgdx.graph.data;

public interface Graph<T extends GraphNode, U extends GraphConnection, V extends GraphProperty> {
    T getNodeById(String id);

    V getPropertyByName(String name);

    Iterable<? extends U> getConnections();

    Iterable<? extends T> getNodes();

    Iterable<? extends V> getProperties();
}

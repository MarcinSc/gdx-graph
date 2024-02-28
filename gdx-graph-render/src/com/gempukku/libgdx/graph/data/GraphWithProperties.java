package com.gempukku.libgdx.graph.data;

import com.gempukku.libgdx.graph.data.Graph;

public interface GraphWithProperties extends Graph {
    Iterable<? extends GraphProperty> getProperties();
}

package com.gempukku.libgdx.graph.ui;

public interface GraphResolver {
    GraphInformation findGraphByPath(String path);

    Iterable<? extends GraphInformation> getGraphsByType(String graphType);

    interface GraphInformation {
        String getPath();

        String getName();

        String getGraphType();
    }
}

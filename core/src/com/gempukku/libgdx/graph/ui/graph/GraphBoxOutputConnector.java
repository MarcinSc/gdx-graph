package com.gempukku.libgdx.graph.ui.graph;

public interface GraphBoxOutputConnector {
    enum Side {
        Right, Bottom
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}

package com.gempukku.libgdx.graph.ui.graph;

public interface GraphBoxInputConnector {
    enum Side {
        Left, Top
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}

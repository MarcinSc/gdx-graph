package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;

public interface GraphChangedAware {
    void graphChanged(GraphChangedEvent event, GraphWithProperties graph);
}

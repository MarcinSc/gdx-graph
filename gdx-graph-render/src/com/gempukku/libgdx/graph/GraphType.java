package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.validator.GraphValidationResult;

public interface GraphType {
    String getType();

    GraphValidationResult validateGraph(Graph graph);
}

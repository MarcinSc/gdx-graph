package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.ui.graph.validator.GraphValidator;

public interface GraphType {
    String getType();

    String getStartNodeIdForValidation();

    GraphValidator getGraphValidator();

    PropertyLocation[] getPropertyLocations();
}

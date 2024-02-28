package com.gempukku.libgdx.graph.config;

import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;
import com.gempukku.libgdx.graph.data.impl.DefaultNodeConfiguration;

public class PropertyNodeConfiguration extends DefaultNodeConfiguration {
    public PropertyNodeConfiguration(String name, String fieldType) {
        super("Property", name);
        addNodeOutput(new DefaultGraphNodeOutput("value", name, fieldType));
    }
}

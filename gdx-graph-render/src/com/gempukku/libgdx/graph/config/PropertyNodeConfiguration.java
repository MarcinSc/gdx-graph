package com.gempukku.libgdx.graph.config;

import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeConfiguration;

public class PropertyNodeConfiguration extends DefaultNodeConfiguration {
    private final String name;
    private final String fieldType;

    public PropertyNodeConfiguration(String name, String fieldType) {
        super("Property", name);
        this.name = name;
        this.fieldType = fieldType;
        addNodeOutput(new DefaultGraphNodeOutput("value", name, fieldType));
    }
}

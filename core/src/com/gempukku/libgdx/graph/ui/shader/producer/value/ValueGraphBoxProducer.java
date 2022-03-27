package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

public abstract class ValueGraphBoxProducer implements GraphBoxProducer {
    protected NodeConfiguration configuration;

    public ValueGraphBoxProducer(NodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public final String getType() {
        return configuration.getType();
    }

    @Override
    public final boolean isCloseable() {
        return true;
    }

    @Override
    public final String getName() {
        return configuration.getName();
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }
}

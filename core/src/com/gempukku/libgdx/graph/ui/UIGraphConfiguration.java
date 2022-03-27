package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

import java.util.Map;

public interface UIGraphConfiguration {
    Iterable<GraphBoxProducer> getGraphBoxProducers();

    Map<String, PropertyBoxProducer> getPropertyBoxProducers();
}

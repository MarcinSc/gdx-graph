package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Map;

public interface UIGraphConfiguration {
    Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers();

    Map<String, ? extends PropertyEditorDefinition> getPropertyEditorDefinitions();
}

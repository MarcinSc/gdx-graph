package com.gempukku.libgdx.graph.ui.pipeline;

import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Map;
import java.util.TreeMap;

public class UIRenderPipelineConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new TreeMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, ? extends PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}

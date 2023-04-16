package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.config.provided.*;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UIModelShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new LinkedHashMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndModelShaderBoxProducer());

        register(new DefaultMenuGraphNodeEditorProducer(new WorldPositionShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ObjectToWorldShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ObjectNormalToWorldShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ModelFragmentCoordinateShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new InstanceIdShaderNodeConfiguration()));
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}

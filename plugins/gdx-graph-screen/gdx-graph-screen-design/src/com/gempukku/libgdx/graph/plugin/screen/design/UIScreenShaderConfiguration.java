package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.plugin.screen.design.producer.EndScreenShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIScreenShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndScreenShaderBoxProducer());
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return Collections.emptyMap();
    }
}

package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.plugin.screen.design.producer.EndScreenShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIScreenShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndScreenShaderBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}

package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.config.provided.*;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UIModelShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        GraphTypeRegistry.registerType(ModelShaderGraphType.instance);

        register(new EndModelShaderBoxProducer());

        register(new GraphBoxProducerImpl(new WorldPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ObjectToWorldShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ObjectNormalToWorldShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ModelFragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new InstanceIdShaderNodeConfiguration()));
    }

    public static void registerPropertyType(PropertyBoxProducer propertyBoxProducer) {
        propertyProducers.put(propertyBoxProducer.getType(), propertyBoxProducer);
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return propertyProducers;
    }
}

package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.config.provided.*;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.SkinningShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIModelShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndModelShaderBoxProducer());

        register(new GraphBoxProducerImpl(new WorldPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ObjectToWorldShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ObjectNormalToWorldShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ModelFragmentCoordinateShaderNodeConfiguration()));
        register(new SkinningShaderBoxProducer());
        register(new GraphBoxProducerImpl(new InstanceIdShaderNodeConfiguration()));
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

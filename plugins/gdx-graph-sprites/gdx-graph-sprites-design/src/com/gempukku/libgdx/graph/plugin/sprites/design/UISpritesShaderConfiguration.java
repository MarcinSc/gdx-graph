package com.gempukku.libgdx.graph.plugin.sprites.design;

import com.gempukku.libgdx.graph.plugin.sprites.config.SpritePositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpriteUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.sprites.design.producer.EndSpriteShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UISpritesShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndSpriteShaderBoxProducer());

        register(new GraphBoxProducerImpl(new SpritePositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SpriteUVShaderNodeConfiguration()));
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

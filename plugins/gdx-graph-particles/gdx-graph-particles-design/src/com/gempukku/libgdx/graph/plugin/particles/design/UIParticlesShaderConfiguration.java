package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration {
    private static final Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static final Map<String, PreviewParticleGeneratorProducer> generatorProducers = new LinkedHashMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerParticleGeneratorProducer(String name, PreviewParticleGeneratorProducer particleGeneratorProducer) {
        generatorProducers.put(name, particleGeneratorProducer);
    }

    public static Map<String, PreviewParticleGeneratorProducer> getParticleGeneratorProducers() {
        return generatorProducers;
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

package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration {
    private static final Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndParticlesShaderBoxProducer());

        register(new GdxGraphNodeEditorProducer(new ParticleLifetimeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ParticleLifePercentageShaderNodeConfiguration()));
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

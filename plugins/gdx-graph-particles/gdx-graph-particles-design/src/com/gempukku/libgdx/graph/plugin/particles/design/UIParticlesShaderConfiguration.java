package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
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

        register(new DefaultMenuGraphNodeEditorProducer(new ParticleLifetimeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ParticleLifePercentageShaderNodeConfiguration()));
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

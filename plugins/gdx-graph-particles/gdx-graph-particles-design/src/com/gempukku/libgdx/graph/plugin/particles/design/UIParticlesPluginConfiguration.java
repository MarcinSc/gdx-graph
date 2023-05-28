package com.gempukku.libgdx.graph.plugin.particles.design;

import java.util.LinkedHashMap;
import java.util.Map;

public class UIParticlesPluginConfiguration {
    private static final Map<String, PreviewParticleGeneratorProducer> generatorProducers = new LinkedHashMap<>();

    public static void registerParticleGeneratorProducer(String name, PreviewParticleGeneratorProducer particleGeneratorProducer) {
        generatorProducers.put(name, particleGeneratorProducer);
    }

    public static Map<String, PreviewParticleGeneratorProducer> getParticleGeneratorProducers() {
        return generatorProducers;
    }
}

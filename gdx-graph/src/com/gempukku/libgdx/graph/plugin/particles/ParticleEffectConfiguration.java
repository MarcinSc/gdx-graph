package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class ParticleEffectConfiguration {
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<String, PropertySource> properties;
    private final int maxNumberOfParticles;

    public ParticleEffectConfiguration(VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> properties,
                                       int maxNumberOfParticles) {
        this.vertexAttributes = vertexAttributes;
        this.properties = properties;
        this.maxNumberOfParticles = maxNumberOfParticles;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return properties;
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }
}

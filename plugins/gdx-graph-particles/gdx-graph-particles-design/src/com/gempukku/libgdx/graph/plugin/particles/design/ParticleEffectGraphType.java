package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class ParticleEffectGraphType extends GraphType {
    public static ParticleEffectGraphType instance = new ParticleEffectGraphType();

    public ParticleEffectGraphType() {
        super("Particle_Effect", true);
    }

    @Override
    public PropertyLocation[] getPropertyLocations() {
        return new PropertyLocation[]{PropertyLocation.Uniform, PropertyLocation.Global_Uniform, PropertyLocation.Attribute};
    }
}

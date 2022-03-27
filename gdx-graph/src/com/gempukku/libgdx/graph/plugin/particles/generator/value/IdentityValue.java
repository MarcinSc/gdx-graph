package com.gempukku.libgdx.graph.plugin.particles.generator.value;

public class IdentityValue implements FloatValue {
    public static final IdentityValue Instance = new IdentityValue();

    private IdentityValue() {
    }

    @Override
    public float getValue(float seed) {
        return seed;
    }
}

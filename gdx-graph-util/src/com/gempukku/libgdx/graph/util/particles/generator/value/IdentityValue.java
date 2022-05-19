package com.gempukku.libgdx.graph.util.particles.generator.value;

public class IdentityValue implements FloatValue {
    public static final IdentityValue Instance = new IdentityValue();

    private IdentityValue() {
    }

    @Override
    public Float getValue(float seed) {
        return seed;
    }
}

package com.gempukku.libgdx.graph.util.particles.generator.value;

public class StaticFloatValue implements FloatValue {
    private final float value;

    public StaticFloatValue(float value) {
        this.value = value;
    }

    @Override
    public Float getValue(float seed) {
        return value;
    }
}

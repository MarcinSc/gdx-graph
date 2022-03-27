package com.gempukku.libgdx.graph.plugin.particles.generator.value;

public class RangeFloatValue implements FloatValue {
    private final float minimum;
    private final float maximum;

    public RangeFloatValue(float minimum, float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public float getValue(float seed) {
        return minimum + (maximum - minimum) * seed;
    }
}

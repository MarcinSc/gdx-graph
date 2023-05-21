package com.gempukku.libgdx.graph.util.particles.generator.value;

public class RangeFloatValue implements FloatValue {
    private final float minimum;
    private final float maximum;

    public RangeFloatValue(float minimum, float maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public Float getValue(float seed) {
        return minimum + (maximum - minimum) * seed;
    }
}

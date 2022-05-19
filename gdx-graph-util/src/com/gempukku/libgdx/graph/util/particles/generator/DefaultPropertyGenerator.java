package com.gempukku.libgdx.graph.util.particles.generator;

import com.gempukku.libgdx.graph.util.particles.generator.value.PropertyValue;

public class DefaultPropertyGenerator implements PropertyGenerator {
    private Object value;

    public DefaultPropertyGenerator(Object value) {
        this.value = value;
    }

    @Override
    public Object generateProperty(float seed) {
        if (value instanceof PropertyValue)
            return ((PropertyValue) value).getValue(seed);
        return value;
    }
}

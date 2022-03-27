package com.gempukku.libgdx.graph.pipeline.impl;

import com.gempukku.libgdx.graph.pipeline.PipelineProperty;

import java.util.function.Supplier;

public class WritablePipelineProperty implements PipelineProperty {
    private boolean useDefault = true;
    private boolean initialized = false;
    private Object value;

    private final String propertyType;
    private final Supplier<?> defaultValueSupplier;

    public WritablePipelineProperty(String propertyType, Supplier<?> defaultValueSupplier) {
        this.propertyType = propertyType;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public String getType() {
        return propertyType;
    }

    public void setValue(Object value) {
        this.value = value;
        useDefault = false;
    }

    public void unsetValue() {
        if (!useDefault) {
            useDefault = true;
            initialized = false;
        }
    }

    @Override
    public Object getValue() {
        if (!initialized && useDefault) {
            value = defaultValueSupplier.get();
            initialized = true;
        }
        return value;
    }
}

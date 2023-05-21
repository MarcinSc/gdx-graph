package com.gempukku.libgdx.graph.pipeline.impl;

import com.gempukku.libgdx.graph.pipeline.PipelineProperty;

import java.util.function.Supplier;

public class DefaultPipelineProperty implements PipelineProperty {
    private final String propertyType;
    private final Supplier<?> defaultValueSupplier;

    public DefaultPipelineProperty(String propertyType, Supplier<?> defaultValueSupplier) {
        this.propertyType = propertyType;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public String getType() {
        return propertyType;
    }
    @Override
    public Object getValue() {
        return defaultValueSupplier.get();
    }
}

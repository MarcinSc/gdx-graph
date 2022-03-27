package com.gempukku.libgdx.graph.data;

public interface FieldType {
    boolean accepts(Object value);

    Object convert(Object value);

    String getName();

    boolean isTexture();
}

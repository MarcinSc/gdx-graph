package com.gempukku.libgdx.graph.ui.part;

import java.util.function.Function;

public class ToStringEnum<T extends Enum<T>> implements Function<T, String> {
    @Override
    public String apply(T t) {
        return t.toString();
    }
}

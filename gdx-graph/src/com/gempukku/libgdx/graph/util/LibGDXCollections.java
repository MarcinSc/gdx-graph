package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class LibGDXCollections {
    public static <T> ObjectSet<T> singleton(T o) {
        ObjectSet<T> result = new ObjectSet<>();
        result.add(o);
        return result;
    }

    public static <K, V> ObjectMap<K, V> singletonMap(K key, V value) {
        ObjectMap<K, V> result = new ObjectMap<>();
        result.put(key, value);
        return result;
    }

    public static <K, V> ObjectMap<K, V> emptyMap() {
        return new ObjectMap<>();
    }
}

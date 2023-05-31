package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class MapUtil {
    public static <Key, Value> ObjectMap<Key, Value> mapOf(Array<Key> keys, Array<Value> values) {
        if (keys.size != values.size)
            throw new IllegalArgumentException("Not matching number of keys and values");
        ObjectMap<Key, Value> result = new ObjectMap<>();
        for (int i = 0; i < keys.size; i++) {
            result.put(keys.get(i), values.get(i));
        }
        return result;
    }
}

package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class UIGdxGraphPluginRegistry {
    private static final Array<Class<? extends UIGdxGraphPlugin>> plugins = new Array<>();

    public static void register(Class<? extends UIGdxGraphPlugin> clazz) {
        plugins.add(clazz);
    }

    public static void initializePlugins() throws ReflectionException {
        for (Class<? extends UIGdxGraphPlugin> plugin : plugins) {
            UIGdxGraphPlugin gdxGraphPlugin = ClassReflection.newInstance(plugin);
            gdxGraphPlugin.initialize();
        }
    }
}

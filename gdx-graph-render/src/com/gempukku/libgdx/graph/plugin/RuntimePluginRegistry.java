package com.gempukku.libgdx.graph.plugin;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class RuntimePluginRegistry implements Disposable {
    private static final ObjectSet<Class<? extends PluginRuntimeInitializer>> plugins = new ObjectSet<>();
    private final ObjectSet<Disposable> resources = new ObjectSet<>();

    public static void register(Class<? extends PluginRuntimeInitializer> clazz) {
        plugins.add(clazz);
    }

    public static RuntimePluginRegistry initializePlugins() throws ReflectionException {
        RuntimePluginRegistry result = new RuntimePluginRegistry();
        for (Class<? extends PluginRuntimeInitializer> plugin : plugins) {
            PluginRuntimeInitializer pluginRuntimeInitializer = ClassReflection.newInstance(plugin);
            pluginRuntimeInitializer.initialize();
            result.resources.add(pluginRuntimeInitializer);
        }
        return result;
    }

    @Override
    public void dispose() {
        for (Disposable resource : resources) {
            resource.dispose();
        }
    }
}

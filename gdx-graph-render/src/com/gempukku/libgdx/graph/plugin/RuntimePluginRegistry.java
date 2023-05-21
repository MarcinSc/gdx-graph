package com.gempukku.libgdx.graph.plugin;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;

public class RuntimePluginRegistry implements PluginRegistry, Disposable {
    private static final ObjectSet<Class<? extends PluginRuntimeInitializer>> plugins = new ObjectSet<>();

    public static void register(Class<? extends PluginRuntimeInitializer> clazz) {
        plugins.add(clazz);
    }

    public static RuntimePluginRegistry initializePlugins() throws ReflectionException {
        RuntimePluginRegistry result = new RuntimePluginRegistry();
        for (Class<? extends PluginRuntimeInitializer> plugin : plugins) {
            PluginRuntimeInitializer pluginRuntimeInitializer = ClassReflection.newInstance(plugin);
            pluginRuntimeInitializer.initialize(result);
            result.resources.add(pluginRuntimeInitializer);
        }
        return result;
    }

    private final ObjectSet<Disposable> resources = new ObjectSet<>();
    private final ObjectMap<String, RuntimePipelinePlugin> privateData = new ObjectMap<>();
    private final ObjectMap<String, Object> publicData = new ObjectMap<>();

    @Override
    public <T> void registerPublicData(Class<T> clazz, T value) {
        publicData.put(clazz.getName(), value);
    }

    @Override
    public <T extends RuntimePipelinePlugin> void registerPrivateData(Class<T> clazz, T value) {
        privateData.put(clazz.getName(), value);
    }

    @Override
    public <T> T getPrivatePluginData(Class<T> clazz) {
        return getPrivateData(clazz);
    }

    public <T> T getPrivateData(Class<T> clazz) {
        return (T) privateData.get(clazz.getName());
    }

    public <T> T getPublicData(Class<T> clazz) {
        return (T) publicData.get(clazz.getName());
    }

    public void update(TimeProvider timeProvider) {
        for (RuntimePipelinePlugin plugin : privateData.values()) {
            plugin.update(timeProvider);
        }
    }

    @Override
    public void dispose() {
        for (Disposable resource : resources) {
            resource.dispose();
        }
    }
}

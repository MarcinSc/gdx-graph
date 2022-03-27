package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfigurer;

public class PluginRegistry {
    private static Array<PluginDefinition> pluginDefinitionArray = new Array<>();

    public static void addPluginDefinition(PluginDefinition pluginDefinition) {
        pluginDefinitionArray.add(pluginDefinition);
    }

    public static Iterable<PluginDefinition> getPluginDefinitions() {
        return pluginDefinitionArray;
    }

    public static void initializePlugins() throws ReflectionException {
        for (PluginDefinition plugin : pluginDefinitionArray) {
            Class<? extends PluginDesignInitializer> pluginClass = plugin.pluginClass;
            String jsonPlugin = plugin.jsonPlugin;
            if (pluginClass != null) {
                try {
                    PluginDesignInitializer pluginDesignInitializer = ClassReflection.newInstance(pluginClass);
                    pluginDesignInitializer.initialize();
                    plugin.loaded = true;
                } catch (Exception exp) {
                    Gdx.app.error("Plugins", "Unable to load plugin - " + plugin.pluginName, exp);
                }
            } else if (jsonPlugin != null) {
                JsonReader reader = new JsonReader();
                FileHandle jsonFile = plugin.jsonPluginResolver.resolve(jsonPlugin);
                JsonValue pluginDef = reader.parse(jsonFile);
                UIPipelineConfigurer.processPipelineConfig(pluginDef);
            }
        }
    }
}

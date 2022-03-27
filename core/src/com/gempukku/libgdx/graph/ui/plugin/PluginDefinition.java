package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;

public class PluginDefinition {
    public String jarPath;
    public Class<? extends PluginDesignInitializer> pluginClass;
    public FileHandleResolver jsonPluginResolver;
    public String jsonPlugin;
    public String pluginName;
    public String pluginVersion;
    public boolean loaded;
    public boolean canBeRemoved;

    public PluginDefinition(String jarPath, Class<? extends PluginDesignInitializer> pluginClass,
                            String pluginName, String pluginVersion,
                            boolean loaded, boolean canBeRemoved) {
        this.jarPath = jarPath;
        this.pluginClass = pluginClass;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.loaded = loaded;
        this.canBeRemoved = canBeRemoved;
    }

    public PluginDefinition(String jarPath, FileHandleResolver jsonPluginResolver, String jsonPlugin,
                            String pluginName, String pluginVersion,
                            boolean loaded, boolean canBeRemoved) {
        this.jarPath = jarPath;
        this.jsonPluginResolver = jsonPluginResolver;
        this.jsonPlugin = jsonPlugin;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.loaded = loaded;
        this.canBeRemoved = canBeRemoved;
    }
}

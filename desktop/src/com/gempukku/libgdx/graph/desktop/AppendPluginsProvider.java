package com.gempukku.libgdx.graph.desktop;

import com.gempukku.gdx.plugins.Plugin;
import com.gempukku.gdx.plugins.PluginsProvider;

import java.util.ArrayList;
import java.util.List;

public class AppendPluginsProvider<T, U extends Plugin<T>> implements PluginsProvider<T, U> {
    private PluginsProvider<T, U> sourcePlugins;

    private boolean addedFromSource;
    private List<U> plugins = new ArrayList<>();

    public AppendPluginsProvider(PluginsProvider<T, U> sourcePlugins) {
        this.sourcePlugins = sourcePlugins;
    }

    public void appendPlugin(U plugin) {
        plugins.add(plugin);
    }

    @Override
    public Iterable<U> getPlugins() {
        if (!addedFromSource) {
            for (U sourcePlugin : sourcePlugins.getPlugins()) {
                plugins.add(sourcePlugin);
            }
        }

        return plugins;
    }
}

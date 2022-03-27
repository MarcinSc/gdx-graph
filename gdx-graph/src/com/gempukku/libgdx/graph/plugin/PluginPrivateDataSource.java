package com.gempukku.libgdx.graph.plugin;

public interface PluginPrivateDataSource {
    <T> T getPrivatePluginData(Class<T> clazz);
}

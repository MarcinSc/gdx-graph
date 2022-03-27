package com.gempukku.libgdx.graph.plugin;

import com.badlogic.gdx.utils.Disposable;

public interface PluginRuntimeInitializer extends Disposable {
    void initialize(PluginRegistry pluginRegistry);
}

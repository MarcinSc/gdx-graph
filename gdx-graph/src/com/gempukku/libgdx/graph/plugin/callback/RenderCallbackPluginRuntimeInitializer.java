package com.gempukku.libgdx.graph.plugin.callback;

import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class RenderCallbackPluginRuntimeInitializer implements PluginRuntimeInitializer {
    public static void register() {
        PluginRegistryImpl.register(RenderCallbackPluginRuntimeInitializer.class);
    }

    private final RenderCallbackPrivateData data = new RenderCallbackPrivateData();

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        pluginRegistry.registerPrivateData(RenderCallbackPrivateData.class, data);
        pluginRegistry.registerPublicData(RenderCallbackPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}

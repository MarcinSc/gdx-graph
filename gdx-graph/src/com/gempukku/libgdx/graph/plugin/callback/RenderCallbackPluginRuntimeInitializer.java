package com.gempukku.libgdx.graph.plugin.callback;

import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class RenderCallbackPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RenderCallbackPrivateData data = new RenderCallbackPrivateData();
        pluginRegistry.registerPrivateData(RenderCallbackPrivateData.class, data);
        pluginRegistry.registerPublicData(RenderCallbackPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}

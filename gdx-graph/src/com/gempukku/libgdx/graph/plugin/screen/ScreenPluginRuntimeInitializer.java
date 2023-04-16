package com.gempukku.libgdx.graph.plugin.screen;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class ScreenPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private final GraphScreenShadersImpl data = new GraphScreenShadersImpl();

    public static void register() {
        GraphTypeRegistry.registerType(new ScreenShaderGraphType());

        PluginRegistryImpl.register(ScreenPluginRuntimeInitializer.class);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new ScreenShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphScreenShadersImpl.class, data);
        pluginRegistry.registerPublicData(GraphScreenShaders.class, data);
    }

    @Override
    public void dispose() {

    }
}

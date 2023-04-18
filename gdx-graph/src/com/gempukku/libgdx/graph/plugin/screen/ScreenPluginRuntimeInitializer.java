package com.gempukku.libgdx.graph.plugin.screen;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;

public class ScreenPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private final GraphScreenShadersImpl data = new GraphScreenShadersImpl();

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphTypeRegistry.registerType(new ScreenShaderGraphType());

        // End
        ScreenShaderConfiguration.addNodeBuilder(new EndScreenShaderNodeBuilder());

        RendererPipelineConfiguration.register(new ScreenShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphScreenShadersImpl.class, data);
        pluginRegistry.registerPublicData(GraphScreenShaders.class, data);
    }

    @Override
    public void dispose() {

    }
}

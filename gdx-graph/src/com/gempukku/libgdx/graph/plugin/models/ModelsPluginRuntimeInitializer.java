package com.gempukku.libgdx.graph.plugin.models;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderRendererPipelineNodeProducer;

public class ModelsPluginRuntimeInitializer implements PluginRuntimeInitializer {
    public static void register() {
        PluginRegistryImpl.register(ModelsPluginRuntimeInitializer.class);
    }

    public ModelsPluginRuntimeInitializer() {
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphModelsImpl data = new GraphModelsImpl();

        RendererPipelineConfiguration.register(new ModelShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphModelsImpl.class, data);
        pluginRegistry.registerPublicData(GraphModels.class, data);
    }

    @Override
    public void dispose() {

    }
}

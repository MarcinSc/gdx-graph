package com.gempukku.libgdx.graph.plugin.models;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.EndModelShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.plugin.models.provided.*;

public class ModelsPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphTypeRegistry.registerType(new ModelShaderGraphType());

        // End
        ModelShaderConfiguration.addNodeBuilder(new EndModelShaderNodeBuilder());

        // Provided
        ModelShaderConfiguration.addNodeBuilder(new WorldPositionShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ObjectToWorldShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ObjectNormalToWorldShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ModelFragmentCoordinateShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new InstanceIdShaderNodeBuilder());

        GraphModelsImpl data = new GraphModelsImpl();

        RendererPipelineConfiguration.register(new ModelShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphModelsImpl.class, data);
        pluginRegistry.registerPublicData(GraphModels.class, data);
    }

    @Override
    public void dispose() {

    }
}

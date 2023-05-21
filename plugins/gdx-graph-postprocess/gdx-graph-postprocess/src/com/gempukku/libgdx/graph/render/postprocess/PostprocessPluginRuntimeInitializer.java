package com.gempukku.libgdx.graph.render.postprocess;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.postprocess.producer.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.postprocess.producer.DepthOfFieldPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.postprocess.producer.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.postprocess.producer.GaussianBlurPipelineNodeProducer;

public class PostprocessPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new BloomPipelineNodeProducer());
        RendererPipelineConfiguration.register(new GaussianBlurPipelineNodeProducer());
        RendererPipelineConfiguration.register(new DepthOfFieldPipelineNodeProducer());
        RendererPipelineConfiguration.register(new GammaCorrectionPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}

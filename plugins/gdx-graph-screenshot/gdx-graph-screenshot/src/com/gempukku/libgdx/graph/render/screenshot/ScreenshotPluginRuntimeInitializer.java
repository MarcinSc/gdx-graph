package com.gempukku.libgdx.graph.render.screenshot;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.render.screenshot.producer.ScreenshotPipelineNodeProducer;
import com.gempukku.libgdx.graph.render.screenshot.producer.ScreenshotShadowMapPipelineNodeProducer;

public class ScreenshotPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        RendererPipelineConfiguration.register(new ScreenshotPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ScreenshotShadowMapPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}

package com.gempukku.libgdx.graph.plugin.callback.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.ScreenshotEditorProducer;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.ScreenshotShadowMapEditorProducer;
import com.gempukku.libgdx.graph.render.screenshot.ScreenshotPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIScreenshotPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register node editors
        UIRenderPipelineConfiguration.register(new ScreenshotEditorProducer());
        UIRenderPipelineConfiguration.register(new ScreenshotShadowMapEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ScreenshotPluginRuntimeInitializer.class);
    }
}

package com.gempukku.libgdx.graph.plugin.callback.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.RenderCallbackEditorProducer;
import com.gempukku.libgdx.graph.render.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIRenderCallbackPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register node editors
        UIRenderPipelineConfiguration.register(new RenderCallbackEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(RenderCallbackPluginRuntimeInitializer.class);
    }
}

package com.gempukku.libgdx.graph.plugin.ui.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.ui.design.producer.UIRendererPipelineEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIUserInterfacePlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register node editors
        UIRenderPipelineConfiguration.register(new UIRendererPipelineEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(UIPluginRuntimeInitializer.class);
    }
}

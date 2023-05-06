package com.gempukku.libgdx.graph.plugin.maps.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.maps.MapsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsLayerIdsRendererEditorProducer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsLayersRendererEditorProducer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsRendererEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIMapsPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register node editors
        UIRenderPipelineConfiguration.register(new MapsRendererEditorProducer());
        UIRenderPipelineConfiguration.register(new MapsLayerIdsRendererEditorProducer());
        UIRenderPipelineConfiguration.register(new MapsLayersRendererEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(MapsPluginRuntimeInitializer.class);
    }
}

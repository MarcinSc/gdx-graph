package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.config.provided.*;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIModelsPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register graph type
        GraphTypeRegistry.registerType(new UIModelShaderGraphType(VisUI.getSkin().getDrawable("graph-model-shader-icon")));

        // Register node editors
        UIModelShaderConfiguration.register(new EndModelShaderBoxProducer());

        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new WorldPositionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectNormalToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ModelFragmentCoordinateShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new InstanceIdShaderNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new ModelShaderRendererBoxProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ModelsPluginRuntimeInitializer.class);
    }
}

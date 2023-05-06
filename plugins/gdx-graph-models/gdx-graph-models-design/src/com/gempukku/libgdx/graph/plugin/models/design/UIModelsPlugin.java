package com.gempukku.libgdx.graph.plugin.models.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.config.provided.*;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderEditorProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderRendererEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIModelsPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIModelShaderGraphType graphType = new UIModelShaderGraphType(VisUI.getSkin().getDrawable("graph-model-shader-icon"));
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UIModelShaderConfiguration.register(new EndModelShaderEditorProducer());

        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new WorldPositionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectNormalToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ModelFragmentCoordinateShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new InstanceIdShaderNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new ModelShaderRendererEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ModelsPluginRuntimeInitializer.class);

        ModelsTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty model shader", assetResolver.resolve("template/model/empty-model-shader.json")));
    }
}

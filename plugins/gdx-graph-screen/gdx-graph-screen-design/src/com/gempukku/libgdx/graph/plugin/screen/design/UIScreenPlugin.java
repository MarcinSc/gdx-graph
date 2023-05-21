package com.gempukku.libgdx.graph.plugin.screen.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.EndScreenShaderEditorProducer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.ScreenShaderRendererEditorProducer;
import com.gempukku.libgdx.graph.shader.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIScreenPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIScreenShaderGraphType graphType = new UIScreenShaderGraphType(VisUI.getSkin().getDrawable("graph-screen-shader-icon"));
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UIScreenShaderConfiguration.register(new EndScreenShaderEditorProducer());

        UIRenderPipelineConfiguration.register(new ScreenShaderRendererEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ScreenPluginRuntimeInitializer.class);

        ScreenTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty screen shader", assetResolver.resolve("template/screen/empty-screen-shader.json")));
    }
}

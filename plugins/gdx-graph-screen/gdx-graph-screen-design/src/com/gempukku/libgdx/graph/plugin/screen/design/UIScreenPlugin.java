package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.EndScreenShaderEditorProducer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.ScreenShaderRendererEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIScreenPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register graph type
        GraphTypeRegistry.registerType(new UIScreenShaderGraphType(VisUI.getSkin().getDrawable("graph-screen-shader-icon")));

        // Register node editors
        UIScreenShaderConfiguration.register(new EndScreenShaderEditorProducer());

        UIRenderPipelineConfiguration.register(new ScreenShaderRendererEditorProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ScreenPluginRuntimeInitializer.class);
    }
}

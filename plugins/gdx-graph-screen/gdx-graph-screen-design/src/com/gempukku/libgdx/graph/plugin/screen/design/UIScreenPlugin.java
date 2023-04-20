package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.EndScreenShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.ScreenShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIScreenPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register graph type
        GraphTypeRegistry.registerType(new UIScreenShaderGraphType(VisUI.getSkin().getDrawable("graph-screen-shader-icon")));

        // Register node editors
        UIScreenShaderConfiguration.register(new EndScreenShaderBoxProducer());

        UIRenderPipelineConfiguration.register(new ScreenShaderRendererBoxProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ScreenPluginRuntimeInitializer.class);
    }
}

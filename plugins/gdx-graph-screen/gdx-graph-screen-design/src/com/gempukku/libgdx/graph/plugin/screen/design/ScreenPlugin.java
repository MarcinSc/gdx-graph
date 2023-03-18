package com.gempukku.libgdx.graph.plugin.screen.design;

import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.producer.ScreenShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;

public class ScreenPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-screen";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(1, 0, 0);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(GdxGraphApplication gdxGraphApplication) {
        ScreenPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ScreenShaderRendererBoxProducer());
    }

    @Override
    public void deregisterPlugin() {

    }
}

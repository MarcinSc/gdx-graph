package com.gempukku.libgdx.graph.plugin.callback.design;


import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.RenderCallbackBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;

public class RenderCallbackPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-render-callback";
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
        RenderCallbackPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new RenderCallbackBoxProducer());
    }

    @Override
    public void deregisterPlugin() {

    }
}

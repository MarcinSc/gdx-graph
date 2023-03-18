package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;

public class ModelsPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-models";
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
        ModelsPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ModelShaderRendererBoxProducer());
    }

    @Override
    public void deregisterPlugin() {

    }
}

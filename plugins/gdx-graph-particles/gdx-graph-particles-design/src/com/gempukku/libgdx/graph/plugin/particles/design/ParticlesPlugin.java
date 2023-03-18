package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticlesShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.GdxGraphPlugin;

public class ParticlesPlugin implements GdxGraphPlugin {
    @Override
    public String getId() {
        return "gdx-graph-particles";
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
        ParticlesPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new ParticlesShaderRendererBoxProducer());
    }

    @Override
    public void deregisterPlugin() {

    }
}

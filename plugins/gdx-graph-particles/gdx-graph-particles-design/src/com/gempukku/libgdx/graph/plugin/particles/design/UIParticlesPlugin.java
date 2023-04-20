package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticlesShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;
import com.kotcrab.vis.ui.VisUI;

public class UIParticlesPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register graph type
        GraphTypeRegistry.registerType(new UIParticleEffectGraphType(VisUI.getSkin().getDrawable("graph-particle-effect-icon")));

        // Register node editors
        UIParticlesShaderConfiguration.register(new EndParticlesShaderBoxProducer());
        UIParticlesShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifetimeShaderNodeConfiguration()));
        UIParticlesShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifePercentageShaderNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new ParticlesShaderRendererBoxProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(ParticlesPluginRuntimeInitializer.class);
    }
}

package com.gempukku.libgdx.graph.plugin.sprites.design;

import com.gempukku.libgdx.graph.plugin.sprites.SpritesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.design.producer.SpriteShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class SpritesPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        SpritesPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new SpriteShaderRendererBoxProducer());
    }
}

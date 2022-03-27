package com.gempukku.libgdx.graph.plugin.sprites;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.plugin.sprites.producer.SpriteShaderRendererPipelineNodeProducer;

public class SpritesPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private static int spriteBatchSize;

    public static void register() {
        register(1000);
    }

    public static void register(int spriteBatchSize) {
        SpritesPluginRuntimeInitializer.spriteBatchSize = spriteBatchSize;
        PluginRegistryImpl.register(SpritesPluginRuntimeInitializer.class);
    }

    private final GraphSpritesImpl sprites;

    public SpritesPluginRuntimeInitializer() {
        sprites = new GraphSpritesImpl(spriteBatchSize);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new SpriteShaderRendererPipelineNodeProducer(pluginRegistry));

        pluginRegistry.registerPrivateData(GraphSpritesImpl.class, sprites);
        pluginRegistry.registerPublicData(GraphSprites.class, sprites);
    }

    @Override
    public void dispose() {
        sprites.dispose();
    }
}
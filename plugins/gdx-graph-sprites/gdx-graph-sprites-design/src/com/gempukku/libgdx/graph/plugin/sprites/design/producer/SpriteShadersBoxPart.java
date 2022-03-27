package com.gempukku.libgdx.graph.plugin.sprites.design.producer;

import com.gempukku.libgdx.graph.plugin.sprites.design.SpriteShaderGraphType;
import com.gempukku.libgdx.graph.plugin.sprites.design.SpritesTemplateRegistry;
import com.gempukku.libgdx.graph.plugin.sprites.design.UISpritesShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class SpriteShadersBoxPart extends ShaderGraphBoxPart {
    private static UIGraphConfiguration[] graphConfigurations = new UIGraphConfiguration[]{
            new UISpritesShaderConfiguration(),
            new UICommonShaderConfiguration()
    };

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return SpritesTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return SpriteShaderGraphType.instance;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}

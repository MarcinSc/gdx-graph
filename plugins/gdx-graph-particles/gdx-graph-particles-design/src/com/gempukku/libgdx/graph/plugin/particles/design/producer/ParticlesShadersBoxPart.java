package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.plugin.particles.design.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesTemplateRegistry;
import com.gempukku.libgdx.graph.plugin.particles.design.UIParticlesShaderConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class ParticlesShadersBoxPart extends ShaderGraphBoxPart {
    private static UIGraphConfiguration[] graphConfigurations = new UIGraphConfiguration[]{
            new UIParticlesShaderConfiguration(),
            new UICommonShaderConfiguration()
    };

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ParticlesTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return ParticleEffectGraphType.instance;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}

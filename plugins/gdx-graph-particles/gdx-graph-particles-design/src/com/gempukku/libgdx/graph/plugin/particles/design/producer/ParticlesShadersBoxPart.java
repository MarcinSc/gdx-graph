package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesTemplateRegistry;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry.GraphShaderTemplate;

public class ParticlesShadersBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ParticlesShadersBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType("Particle_Effect");
    }

    @Override
    protected Iterable<GraphShaderTemplate> getTemplates() {
        return ParticlesTemplateRegistry.getTemplates();
    }

    @Override
    protected GraphType getGraphType() {
        return graphType;
    }

    @Override
    protected UIGraphConfiguration[] getGraphConfigurations() {
        return graphType.getUIConfigurations();
    }
}

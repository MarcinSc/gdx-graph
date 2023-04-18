package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ParticlesShadersBoxPart extends ShaderGraphBoxPart {
    private UIGraphType graphType;

    public ParticlesShadersBoxPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ParticleEffectGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

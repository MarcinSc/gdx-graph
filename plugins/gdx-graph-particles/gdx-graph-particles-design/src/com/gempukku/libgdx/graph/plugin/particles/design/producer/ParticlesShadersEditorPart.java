package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class ParticlesShadersEditorPart extends ShaderGraphEditorPart {
    private UIGraphType graphType;

    public ParticlesShadersEditorPart() {
        graphType = (UIGraphType) GraphTypeRegistry.findGraphType(ParticleEffectGraphType.TYPE);
    }

    @Override
    protected UIGraphType getGraphType() {
        return graphType;
    }
}

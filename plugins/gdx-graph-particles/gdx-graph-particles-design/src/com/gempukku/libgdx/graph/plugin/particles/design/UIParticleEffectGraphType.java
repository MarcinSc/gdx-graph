package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIParticleEffectGraphType extends ParticleEffectGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIParticleEffectGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIParticlesShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "pes";
    }

    @Override
    public String getPresentableName() {
        return "Particles shader";
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return ParticlesTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphType;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class UIParticleEffectGraphType extends ParticleEffectGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIParticleEffectGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIParticlesShaderConfiguration(),
                new UICommonShaderConfiguration()};
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}

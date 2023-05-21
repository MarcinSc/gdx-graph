package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class UIParticleEffectGraphType extends ParticleEffectGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;
    private Drawable icon;

    public UIParticleEffectGraphType(Drawable icon) {
        this.icon = icon;
        configurations = new UIGraphConfiguration[]{
                new UIParticlesShaderConfiguration(),
                new UIModelShaderConfiguration()};
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
    public Drawable getIcon() {
        return icon;
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

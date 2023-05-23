package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModel;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModelProducer;
import com.gempukku.libgdx.ui.undo.UndoableSelectBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ParticlePreviewRenderableModelProducer extends VisTable implements PreviewRenderableModelProducer {
    private final UndoableSelectBox<String> generator;
    private final VisSlider lifetime;
    private final VisSlider initialCount;
    private final VisSlider perSecondCount;

    private ParticlePreviewRenderableModel lastModel;

    public ParticlePreviewRenderableModelProducer() {
        VisLabel generatorText = new VisLabel("Generator: ", "gdx-graph-property-label");
        generator = new UndoableSelectBox<>("gdx-graph-property");
        generator.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (lastModel != null) {
                            lastModel.setParticleGeneratorProducer(getGenerator());
                        }
                    }
                });

        Array<String> generatorNames = new Array<>();
        for (String generatorName : UIParticlesShaderConfiguration.getParticleGeneratorProducers().keySet()) {
            generatorNames.add(generatorName);
        }
        generator.setItems(generatorNames);

        VisLabel lifetimeText = new VisLabel("Lifetime: 3.00", "gdx-graph-property-label");
        lifetime = new VisSlider(0f, 10f, 0.01f, false, "gdx-graph");
        lifetime.setValue(3f);

        VisLabel initialCountText = new VisLabel("Initial count: 0", "gdx-graph-property-label");
        initialCount = new VisSlider(0f, 100f, 1f, false, "gdx-graph");
        initialCount.setValue(0f);

        VisLabel perSecondCountText = new VisLabel("Per second count: 10", "gdx-graph-property-label");
        perSecondCount = new VisSlider(0, 100f, 0.1f, false, "gdx-graph");
        perSecondCount.setValue(10f);

        VisTextButton resetButton = new VisTextButton("Reset particles", "gdx-graph-property-label");
        resetButton.padLeft(10).padRight(10);

        resetButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (lastModel != null) {
                            lastModel.resetParticles();
                        }
                    }
                });

        ChangeListener lifetimeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lastModel != null) {
                    lastModel.setLifeLength(getLifeLength());
                }
            }
        };
        lifetime.addListener(lifetimeListener);

        ChangeListener initialCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lastModel != null) {
                    lastModel.setInitialParticles(getInitialParticles());
                }
            }
        };
        initialCount.addListener(initialCountListener);

        ChangeListener perSecondCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lastModel != null) {
                    lastModel.setParticlesPerSecond(getParticlesPerSecond());
                }
            }
        };
        perSecondCount.addListener(perSecondCountListener);

        add(generatorText).growX().row();
        add(generator).growX().row();
        add(lifetimeText).growX().row();
        add(lifetime).growX().row();
        add(initialCountText).growX().row();
        add(initialCount).growX().row();
        add(perSecondCountText).growX().row();
        add(perSecondCount).growX().row();
        add(resetButton).center().pad(2).row();
    }

    private PreviewParticleGeneratorProducer getGenerator() {
        return UIParticlesShaderConfiguration.getParticleGeneratorProducers().get(generator.getSelected());
    }

    private float getLifeLength() {
        return lifetime.getValue();
    }

    private int getInitialParticles() {
        return MathUtils.round(initialCount.getValue());
    }

    private float getParticlesPerSecond() {
        return perSecondCount.getValue();
    }

    @Override
    public Actor getCustomizationActor() {
        return this;
    }

    @Override
    public PreviewRenderableModel create() {
        lastModel = new ParticlePreviewRenderableModel(getGenerator(), getLifeLength(), getInitialParticles(), getParticlesPerSecond());
        return lastModel;
    }
}

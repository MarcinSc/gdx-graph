package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModel;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModelProducer;
import com.gempukku.libgdx.ui.undo.UndoableSelectBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ParticlePreviewRenderableModelProducer extends VisTable implements PreviewRenderableModelProducer {
    public static final String GENERATOR_FIELD = "particle.generator";
    public static final String LIFE_LENGTH_FIELD = "particle.lifeLength";
    public static final String INITIAL_PARTICLES_FIELD = "particle.initialParticles";
    public static final String PARTICLES_PER_SECOND_FIELD = "particle.particlesPerSecond";

    private final UndoableSelectBox<String> generator;
    private final VisSlider lifetime;
    private final VisSlider initialCount;
    private final VisSlider perSecondCount;

    private ParticlePreviewRenderableModel lastModel;

    public ParticlePreviewRenderableModelProducer() {
        VisLabel generatorText = new VisLabel("Generator: ", "gdx-graph-property-label");
        generator = new UndoableSelectBox<>("gdx-graph-property");
        Array<String> generatorNames = new Array<>();
        for (String generatorName : UIParticlesPluginConfiguration.getParticleGeneratorProducers().keySet()) {
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

        generator.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (lastModel != null) {
                            lastModel.setParticleGeneratorProducer(getGenerator());
                        }
                    }
                });

        ChangeListener lifetimeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float lifeLength = getLifeLength();
                lifetimeText.setText("Lifetime: " + SimpleNumberFormatter.format(lifeLength, 2));
                if (lastModel != null) {
                    lastModel.setLifeLength(lifeLength);
                }
            }
        };
        lifetime.addListener(lifetimeListener);

        ChangeListener initialCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int initialParticles = getInitialParticles();
                initialCountText.setText("Initial count: " + initialParticles);
                if (lastModel != null) {
                    lastModel.setInitialParticles(initialParticles);
                }
            }
        };
        initialCount.addListener(initialCountListener);

        ChangeListener perSecondCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float particlesPerSecond = getParticlesPerSecond();
                perSecondCountText.setText("Per second count: " + SimpleNumberFormatter.format(particlesPerSecond, 2));
                if (lastModel != null) {
                    lastModel.setParticlesPerSecond(particlesPerSecond);
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

    @Override
    public void initialize(JsonValue data) {
        if (data != null) {
            setGenerator(data.getString(GENERATOR_FIELD, ""));
            setLifeLength(data.getFloat(LIFE_LENGTH_FIELD, 3f));
            setInitialParticles(data.getInt(INITIAL_PARTICLES_FIELD, 0));
            setParticlesPerSecond(data.getFloat(PARTICLES_PER_SECOND_FIELD, 10f));
        }
    }

    public void setParticlesPerSecond(float particlesPerSecond) {
        perSecondCount.setValue(particlesPerSecond);
    }

    private void setInitialParticles(int initialParticles) {
        initialCount.setValue(initialParticles);
    }


    private void setLifeLength(float lifeLength) {
        lifetime.setValue(lifeLength);
    }

    private void setGenerator(String generator) {
        this.generator.setSelected(generator);
    }

    @Override
    public void serialize(JsonValue value) {
        value.addChild(GENERATOR_FIELD, new JsonValue(generator.getSelected()));
        value.addChild(LIFE_LENGTH_FIELD, new JsonValue(getLifeLength()));
        value.addChild(INITIAL_PARTICLES_FIELD, new JsonValue(getInitialParticles()));
        value.addChild(PARTICLES_PER_SECOND_FIELD, new JsonValue(getParticlesPerSecond()));
    }

    private PreviewParticleGeneratorProducer getGenerator() {
        return UIParticlesPluginConfiguration.getParticleGeneratorProducers().get(generator.getSelected());
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

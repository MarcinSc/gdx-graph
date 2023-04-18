package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ParticlesShaderPreviewBoxPart extends VisTable implements GraphNodeEditorPart, GraphChangedAware, Disposable {
    private final ParticlesShaderPreviewWidget shaderPreviewWidget;

    public ParticlesShaderPreviewBoxPart() {
        final VisSelectBox<ParticlesShaderPreviewWidget.ShaderPreviewModel> selectBox = new VisSelectBox<ParticlesShaderPreviewWidget.ShaderPreviewModel>();
        selectBox.setItems(ParticlesShaderPreviewWidget.ShaderPreviewModel.values());

        final VisSlider cameraDistance = new VisSlider(0.5f, 10f, 0.01f, false);
        cameraDistance.setValue(1f);

        final VisSlider lifetime = new VisSlider(0f, 10f, 0.01f, false);
        lifetime.setValue(3f);

        final VisSlider initialCount = new VisSlider(0f, 100f, 1f, false);
        initialCount.setValue(0f);

        final VisSlider perSecondCount = new VisSlider(0, 100f, 0.1f, false);
        perSecondCount.setValue(10f);

        final VisTextButton resetButton = new VisTextButton("Reset particles");
        resetButton.padLeft(10).padRight(10);

        shaderPreviewWidget = new ParticlesShaderPreviewWidget(300, 300);
        shaderPreviewWidget.setModel(ParticlesShaderPreviewWidget.ShaderPreviewModel.Point);
        shaderPreviewWidget.setCameraDistance(1f);
        shaderPreviewWidget.setLifetime(3f);
        shaderPreviewWidget.setInitialCount(0);
        shaderPreviewWidget.setParticlesPerSecond(10f);

        resetButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.resetParticles();
                    }
                });
        cameraDistance.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setCameraDistance(cameraDistance.getValue());
                    }
                });
        lifetime.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setLifetime(lifetime.getValue());
                    }
                });
        initialCount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setInitialCount(MathUtils.round(initialCount.getValue()));
                    }
                });
        perSecondCount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setParticlesPerSecond(perSecondCount.getValue());
                    }
                });
        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setModel(selectBox.getSelected());
                    }
                });

        add("Shape: ");
        add(selectBox).growX().row();
        add("Lifetime:").colspan(2).growX().row();
        add(lifetime).colspan(2).growX().row();
        add("Initial count:").colspan(2).growX().row();
        add(initialCount).colspan(2).growX().row();
        add("Per second count:").colspan(2).growX().row();
        add(perSecondCount).colspan(2).growX().row();
        add("Camera distance:").colspan(2).growX().row();
        add(cameraDistance).colspan(2).growX().row();
        add(resetButton).colspan(2).center().pad(2).row();
        add(shaderPreviewWidget).colspan(2).grow().row();
    }

    public void initialize(JsonValue data) {
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JsonValue object) {
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        if (event.isStructure() || event.isData()) {
            shaderPreviewWidget.graphChanged(hasErrors, graph);
        }
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}
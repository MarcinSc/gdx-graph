package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;

public class ParticlesShaderPreviewBoxPart extends VisTable implements GraphBoxPart {
    private final ParticlesShaderPreviewWidget shaderPreviewWidget;
    private final VisSelectBox<ParticlesShaderPreviewWidget.ShaderPreviewModel> selectBox;

    public ParticlesShaderPreviewBoxPart() {
        final VisCheckBox running = new VisCheckBox("Running");
        running.setChecked(true);
        running.align(Align.left);
        selectBox = new VisSelectBox<ParticlesShaderPreviewWidget.ShaderPreviewModel>();
        selectBox.setItems(ParticlesShaderPreviewWidget.ShaderPreviewModel.values());

        final VisSlider cameraDistance = new VisSlider(0.5f, 10f, 0.01f, false);
        cameraDistance.setValue(1f);

        final VisSlider lifetime = new VisSlider(0f, 10f, 0.01f, false);
        lifetime.setValue(3f);

        final VisSlider initialCount = new VisSlider(0f, 100f, 1f, false);
        initialCount.setValue(0f);

        final VisSlider perSecondCount = new VisSlider(0, 100f, 0.1f, false);
        perSecondCount.setValue(10f);

        shaderPreviewWidget = new ParticlesShaderPreviewWidget(300, 300);
        shaderPreviewWidget.setRunning(true);
        shaderPreviewWidget.setCameraDistance(1f);
        shaderPreviewWidget.setLifetime(3f);
        shaderPreviewWidget.setInitialCount(0f);
        shaderPreviewWidget.setPerSecondCount(10f);

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
                        shaderPreviewWidget.setInitialCount(initialCount.getValue());
                    }
                });
        perSecondCount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setPerSecondCount(perSecondCount.getValue());
                    }
                });
        running.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setRunning(running.isChecked());
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
        add(running).colspan(2).left().growX().row();
        add(shaderPreviewWidget).colspan(2).grow().row();
    }

    public void initialize(JsonValue data) {
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    @Override
    public void serializePart(JsonValue object) {
    }

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
        shaderPreviewWidget.graphChanged(hasErrors, graph);
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
    }
}
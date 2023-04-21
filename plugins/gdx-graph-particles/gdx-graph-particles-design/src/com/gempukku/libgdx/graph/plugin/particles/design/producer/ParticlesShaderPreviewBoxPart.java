package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.*;

public class ParticlesShaderPreviewBoxPart extends VisTable implements GraphNodeEditorPart, GraphChangedAware, Disposable {
    private final ParticlesShaderPreviewWidget shaderPreviewWidget;
    private final VisSlider cameraDistance;
    private final VisSlider lifetime;
    private final VisSlider initialCount;
    private final VisSlider perSecondCount;
    private final VisSelectBox<ParticlesShaderPreviewWidget.ShaderPreviewModel> selectBox;
    private final String cameraDistanceProperty;
    private final String lifetimeProperty;
    private final String initialCountProperty;
    private final String perSecondProperty;
    private final String previewModelProperty;

    public ParticlesShaderPreviewBoxPart(
            String cameraDistanceProperty, String lifetimeProperty, String initialCountProperty,
            String perSecondProperty, String previewModelProperty) {
        this.cameraDistanceProperty = cameraDistanceProperty;
        this.lifetimeProperty = lifetimeProperty;
        this.initialCountProperty = initialCountProperty;
        this.perSecondProperty = perSecondProperty;
        this.previewModelProperty = previewModelProperty;

        selectBox = new VisSelectBox<>("gdx-graph-property");
        selectBox.setItems(ParticlesShaderPreviewWidget.ShaderPreviewModel.values());

        VisLabel lifetimeText = new VisLabel("Lifetime: 3.00", "gdx-graph-property-label");
        lifetime = new VisSlider(0f, 10f, 0.01f, false);
        lifetime.setValue(3f);

        VisLabel initialCountText = new VisLabel("Initial count: 0", "gdx-graph-property-label");
        initialCount = new VisSlider(0f, 100f, 1f, false);
        initialCount.setValue(0f);

        VisLabel perSecondCountText = new VisLabel("Per second count: 10", "gdx-graph-property-label");
        perSecondCount = new VisSlider(0, 100f, 0.1f, false);
        perSecondCount.setValue(10f);

        VisLabel cameraDistanceText = new VisLabel("Camera distance: 1", "gdx-graph-property-label");
        cameraDistance = new VisSlider(0.5f, 10f, 0.01f, false);
        cameraDistance.setValue(1f);

        final VisTextButton resetButton = new VisTextButton("Reset particles","gdx-graph-property-label");
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
                        cameraDistanceText.setText("Camera distance: "+ SimpleNumberFormatter.format(cameraDistance.getValue(), 2));
                    }
                });
        lifetime.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setLifetime(lifetime.getValue());
                        lifetimeText.setText("Lifetime: "+SimpleNumberFormatter.format(lifetime.getValue(), 2));
                    }
                });
        initialCount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setInitialCount(MathUtils.round(initialCount.getValue()));
                        initialCountText.setText("Initial count: "+SimpleNumberFormatter.format(initialCount.getValue(), 0));
                    }
                });
        perSecondCount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setParticlesPerSecond(perSecondCount.getValue());
                        perSecondCountText.setText("Per second count: "+SimpleNumberFormatter.format(perSecondCount.getValue(), 2));
                    }
                });
        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.setModel(selectBox.getSelected());
                    }
                });

        add(new VisLabel("Shape: ", "gdx-graph-property-label"));
        add(selectBox).growX().row();
        add(lifetimeText).colspan(2).growX().row();
        add(lifetime).colspan(2).growX().row();
        add(initialCountText).colspan(2).growX().row();
        add(initialCount).colspan(2).growX().row();
        add(perSecondCountText).colspan(2).growX().row();
        add(perSecondCount).colspan(2).growX().row();
        add(cameraDistanceText).colspan(2).growX().row();
        add(cameraDistance).colspan(2).growX().row();
        add(resetButton).colspan(2).center().pad(2).row();
        add(shaderPreviewWidget).colspan(2).grow().row();
    }

    public void initialize(JsonValue data) {
        float cameraDistance = data.getFloat(cameraDistanceProperty, -1);
        if (cameraDistance != -1) {
            this.cameraDistance.setValue(cameraDistance);
        }
        float lifetime = data.getFloat(lifetimeProperty, -1);
        if (lifetime != -1) {
            this.lifetime.setValue(lifetime);
        }
        int initialCount = data.getInt(initialCountProperty, -1);
        if (initialCount != -1) {
            this.initialCount.setValue(initialCount);
        }
        float perSecondCount = data.getFloat(perSecondProperty, -1);
        if (perSecondCount != -1) {
            this.perSecondCount.setValue(perSecondCount);
        }
        String previewModel = data.getString(previewModelProperty, null);
        if (previewModel != null) {
            this.selectBox.setSelected(ParticlesShaderPreviewWidget.ShaderPreviewModel.valueOf(previewModel));
        }
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
        object.addChild(cameraDistanceProperty, new JsonValue(cameraDistance.getValue()));
        object.addChild(lifetimeProperty, new JsonValue(lifetime.getValue()));
        object.addChild(initialCountProperty, new JsonValue(initialCount.getValue()));
        object.addChild(perSecondProperty, new JsonValue(perSecondCount.getValue()));
        object.addChild(previewModelProperty, new JsonValue(selectBox.getSelected().name()));
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
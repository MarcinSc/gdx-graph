package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.TabControl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.graph.RequestTabOpen;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

public class ParticlesShaderPreviewEditorPart implements GraphNodeEditorPart, GraphChangedAware, Disposable {
    private final String cameraDistanceProperty;
    private final String lifetimeProperty;
    private final String initialCountProperty;
    private final String perSecondProperty;
    private final String previewModelProperty;

    private final ParticlesShaderPreview shaderPreviewWidget;
    private final VisSelectBox<ParticlesShaderPreview.ShaderPreviewModel> selectBox;
    private final VisLabel lifetimeText;
    private final VisSlider lifetime;
    private final VisLabel cameraDistanceText;
    private final VisSlider cameraDistance;
    private final VisLabel initialCountText;
    private final VisSlider initialCount;
    private final VisLabel perSecondCountText;
    private final VisSlider perSecondCount;
    private final VisTextButton resetButton;

    private final ParticlesShaderPreview tabShaderPreviewWidget;
    private final VisSelectBox<ParticlesShaderPreview.ShaderPreviewModel> tabSelectBox;
    private final VisLabel tabLifetimeText;
    private final VisSlider tabLifetime;
    private final VisLabel tabCameraDistanceText;
    private final VisSlider tabCameraDistance;
    private final VisLabel tabInitialCountText;
    private final VisSlider tabInitialCount;
    private final VisLabel tabPerSecondCountText;
    private final VisSlider tabPerSecondCount;
    private final VisTextButton tabResetButton;

    private final VisTable localTable;
    private final VisTable tabTable;
    private final VisImageButton maximizeButton;

    public ParticlesShaderPreviewEditorPart(
            String cameraDistanceProperty, String lifetimeProperty, String initialCountProperty,
            String perSecondProperty, String previewModelProperty) {
        this.cameraDistanceProperty = cameraDistanceProperty;
        this.lifetimeProperty = lifetimeProperty;
        this.initialCountProperty = initialCountProperty;
        this.perSecondProperty = perSecondProperty;
        this.previewModelProperty = previewModelProperty;

        localTable = new VisTable();

        tabTable = new VisTable();
        tabTable.setBackground("darkGrey");
        tabTable.setFillParent(true);

        Drawable maximizeDrawable = VisUI.getSkin().getDrawable("icon-maximize");
        maximizeButton = new VisImageButton(maximizeDrawable);
        maximizeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        openInNewTab();
                    }
                });

        selectBox = new VisSelectBox<>("gdx-graph-property");
        selectBox.setItems(ParticlesShaderPreview.ShaderPreviewModel.values());
        tabSelectBox = new VisSelectBox<>("gdx-graph-property");
        tabSelectBox.setItems(ParticlesShaderPreview.ShaderPreviewModel.values());

        lifetimeText = new VisLabel("Lifetime: 3.00", "gdx-graph-property-label");
        lifetime = new VisSlider(0f, 10f, 0.01f, false, "gdx-graph");
        lifetime.setValue(3f);
        tabLifetimeText = new VisLabel("Lifetime: 3.00", "gdx-graph-property-label");
        tabLifetime = new VisSlider(0f, 10f, 0.01f, false, "gdx-graph");
        tabLifetime.setValue(3f);

        initialCountText = new VisLabel("Initial count: 0", "gdx-graph-property-label");
        initialCount = new VisSlider(0f, 100f, 1f, false, "gdx-graph");
        initialCount.setValue(0f);
        tabInitialCountText = new VisLabel("Initial count: 0", "gdx-graph-property-label");
        tabInitialCount = new VisSlider(0f, 100f, 1f, false, "gdx-graph");
        tabInitialCount.setValue(0f);

        perSecondCountText = new VisLabel("Per second count: 10", "gdx-graph-property-label");
        perSecondCount = new VisSlider(0, 100f, 0.1f, false, "gdx-graph");
        perSecondCount.setValue(10f);
        tabPerSecondCountText = new VisLabel("Per second count: 10", "gdx-graph-property-label");
        tabPerSecondCount = new VisSlider(0, 100f, 0.1f, false, "gdx-graph");
        tabPerSecondCount.setValue(10f);

        cameraDistanceText = new VisLabel("Camera distance: 1", "gdx-graph-property-label");
        cameraDistance = new VisSlider(0.5f, 10f, 0.01f, false, "gdx-graph");
        cameraDistance.setValue(1f);
        tabCameraDistanceText = new VisLabel("Camera distance: 1", "gdx-graph-property-label");
        tabCameraDistance = new VisSlider(0.5f, 10f, 0.01f, false, "gdx-graph");
        tabCameraDistance.setValue(1f);

        resetButton = new VisTextButton("Reset particles", "gdx-graph-property-label");
        resetButton.padLeft(10).padRight(10);
        tabResetButton = new VisTextButton("Reset particles", "gdx-graph-property-label");
        tabResetButton.padLeft(10).padRight(10);

        shaderPreviewWidget = new ParticlesShaderPreview();
        shaderPreviewWidget.setModel(ParticlesShaderPreview.ShaderPreviewModel.Point);
        shaderPreviewWidget.setCameraDistance(1f);
        shaderPreviewWidget.setLifetime(3f);
        shaderPreviewWidget.setInitialCount(0);
        shaderPreviewWidget.setParticlesPerSecond(10f);
        tabShaderPreviewWidget = new ParticlesShaderPreview();
        tabShaderPreviewWidget.setModel(ParticlesShaderPreview.ShaderPreviewModel.Point);
        tabShaderPreviewWidget.setCameraDistance(1f);
        tabShaderPreviewWidget.setLifetime(3f);
        tabShaderPreviewWidget.setInitialCount(0);
        tabShaderPreviewWidget.setParticlesPerSecond(10f);

        resetButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        shaderPreviewWidget.resetParticles();
                    }
                });
        tabResetButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        tabShaderPreviewWidget.resetParticles();
                    }
                });

        ChangeListener cameraDistanceListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = ((VisSlider) actor).getValue();
                setCameraDistance(value);
            }
        };
        cameraDistance.addListener(cameraDistanceListener);
        tabCameraDistance.addListener(cameraDistanceListener);

        ChangeListener lifetimeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = ((VisSlider) actor).getValue();
                setLifetime(value);
            }
        };
        lifetime.addListener(lifetimeListener);
        tabLifetime.addListener(lifetimeListener);

        ChangeListener initialCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int value = MathUtils.round(((VisSlider) actor).getValue());
                setInitialCount(value);
            }
        };
        initialCount.addListener(initialCountListener);
        tabInitialCount.addListener(initialCountListener);

        ChangeListener perSecondCountListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float value = ((VisSlider) actor).getValue();
                setPerSecondCount(value);
            }
        };
        perSecondCount.addListener(perSecondCountListener);
        tabPerSecondCount.addListener(perSecondCountListener);

        ChangeListener shapeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ParticlesShaderPreview.ShaderPreviewModel selected = ((VisSelectBox<ParticlesShaderPreview.ShaderPreviewModel>) actor).getSelected();
                setShape(selected);
            }
        };
        selectBox.addListener(shapeListener);
        tabSelectBox.addListener(shapeListener);

        // Local table creation
        int colspan = 3;
        localTable.add(new VisLabel("Shape: ", "gdx-graph-property-label"));
        localTable.add(selectBox).growX();
        localTable.add(maximizeButton).row();
        localTable.add(lifetimeText).colspan(colspan).growX().row();
        localTable.add(lifetime).colspan(colspan).growX().row();
        localTable.add(cameraDistanceText).colspan(colspan).growX().row();
        localTable.add(cameraDistance).colspan(colspan).growX().row();
        localTable.add(initialCountText).colspan(colspan).growX().row();
        localTable.add(initialCount).colspan(colspan).growX().row();
        localTable.add(perSecondCountText).colspan(colspan).growX().row();
        localTable.add(perSecondCount).colspan(colspan).growX().row();
        localTable.add(resetButton).colspan(colspan).center().pad(2).row();
        localTable.add(shaderPreviewWidget).width(300).height(300).colspan(colspan).grow().row();

        // Tab table creation
        VisTable shapeTable = new VisTable();
        shapeTable.add(new VisLabel("Shape: ", "gdx-graph-property-label"));
        shapeTable.add(tabSelectBox).growX();

        tabTable.add(shapeTable).growX().pad(2);
        tabTable.add(tabResetButton).expandX().center().pad(2).row();

        tabTable.add(tabLifetimeText).growX().pad(2);
        tabTable.add(tabInitialCountText).growX().pad(2).row();
        tabTable.add(tabLifetime).growX().pad(2);
        tabTable.add(tabInitialCount).growX().pad(2).row();

        tabTable.add(tabCameraDistanceText).growX().pad(2);
        tabTable.add(tabPerSecondCountText).growX().pad(2).row();
        tabTable.add(tabCameraDistance).growX().pad(2);
        tabTable.add(tabPerSecondCount).growX().pad(2).row();

        tabTable.add(tabShaderPreviewWidget).colspan(2).grow().row();
    }

    private void setShape(ParticlesShaderPreview.ShaderPreviewModel selected) {
        selectBox.setSelected(selected);
        tabSelectBox.setSelected(selected);
        shaderPreviewWidget.setModel(selectBox.getSelected());
        tabShaderPreviewWidget.setModel(selectBox.getSelected());
    }

    private void setPerSecondCount(float value) {
        perSecondCount.setValue(value);
        tabPerSecondCount.setValue(value);
        perSecondCountText.setText("Per second count: " + SimpleNumberFormatter.format(value, 2));
        tabPerSecondCountText.setText("Per second count: " + SimpleNumberFormatter.format(value, 2));
        shaderPreviewWidget.setParticlesPerSecond(value);
        tabShaderPreviewWidget.setParticlesPerSecond(value);
    }

    private void setInitialCount(int value) {
        initialCount.setValue(value);
        tabInitialCount.setValue(value);
        initialCountText.setText("Initial count: " + SimpleNumberFormatter.format(value, 0));
        tabInitialCountText.setText("Initial count: " + SimpleNumberFormatter.format(value, 0));
        shaderPreviewWidget.setInitialCount(value);
        tabShaderPreviewWidget.setInitialCount(value);
    }

    private void setLifetime(float value) {
        lifetime.setValue(value);
        tabLifetime.setValue(value);
        lifetimeText.setText("Lifetime: " + SimpleNumberFormatter.format(value, 2));
        tabLifetimeText.setText("Lifetime: " + SimpleNumberFormatter.format(value, 2));
        shaderPreviewWidget.setLifetime(value);
        tabShaderPreviewWidget.setLifetime(value);
    }

    private void setCameraDistance(float value) {
        cameraDistance.setValue(value);
        tabCameraDistance.setValue(value);
        cameraDistanceText.setText("Camera distance: " + SimpleNumberFormatter.format(value, 2));
        tabCameraDistanceText.setText("Camera distance: " + SimpleNumberFormatter.format(value, 2));
        shaderPreviewWidget.setCameraDistance(value);
        tabShaderPreviewWidget.setCameraDistance(value);
    }

    private void openInNewTab() {
        localTable.fire(
                new RequestTabOpen("preview", "Preview", null,
                        new Supplier<Table>() {
                            @Override
                            public Table get() {
                                return tabTable;
                            }
                        },
                        new Function<TabControl, AssistantPluginTab>() {
                            @Override
                            public AssistantPluginTab evaluate(TabControl tabControl) {
                                return new AssistantPluginTab() {
                                    @Override
                                    public boolean isDirty() {
                                        return false;
                                    }

                                    @Override
                                    public void setActive(boolean b) {

                                    }

                                    @Override
                                    public void closed() {
                                        tabControl.tabClosed(this);
                                    }
                                };
                            }
                        }));
    }

    public void initialize(JsonValue data) {
        float cameraDistance = data.getFloat(cameraDistanceProperty, -1);
        if (cameraDistance != -1) {
            setCameraDistance(cameraDistance);
        }
        float lifetime = data.getFloat(lifetimeProperty, -1);
        if (lifetime != -1) {
            setLifetime(lifetime);
        }
        int initialCount = data.getInt(initialCountProperty, -1);
        if (initialCount != -1) {
            setInitialCount(initialCount);
        }
        float perSecondCount = data.getFloat(perSecondProperty, -1);
        if (perSecondCount != -1) {
            setPerSecondCount(perSecondCount);
        }
        String previewModel = data.getString(previewModelProperty, null);
        if (previewModel != null) {
            setShape(ParticlesShaderPreview.ShaderPreviewModel.valueOf(previewModel));
        }
    }

    @Override
    public Actor getActor() {
        return localTable;
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
            tabShaderPreviewWidget.graphChanged(hasErrors, graph);
        }
    }

    @Override
    public void dispose() {
        shaderPreviewWidget.dispose();
        tabShaderPreviewWidget.dispose();
    }
}
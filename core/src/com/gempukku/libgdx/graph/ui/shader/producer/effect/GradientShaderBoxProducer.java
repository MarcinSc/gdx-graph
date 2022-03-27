package com.gempukku.libgdx.graph.ui.shader.producer.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.config.common.effect.GradientShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.talosvfx.talos.editor.widgets.GradientWidget;
import com.talosvfx.talos.runtime.values.ColorPoint;

public class GradientShaderBoxProducer extends GraphBoxProducerImpl {
    public GradientShaderBoxProducer() {
        super(new GradientShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        final GradientWidget gradientWidget = new GradientWidget();
        gradientWidget.setSize(300, 40);

        if (data != null) {
            JsonValue points = data.get("points");
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                gradientWidget.createPoint(Color.valueOf(split[0]), Float.parseFloat(split[1]));
            }
        } else {
            gradientWidget.createPoint(Color.WHITE, 0);
        }

        gradientWidget.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        gradientWidget.fire(new GraphChangedEvent(false, true));
                    }
                });

        gradientWidget.setListener(
                new GradientWidget.GradientWidgetListener() {
                    @Override
                    public void colorPickerShow(final ColorPoint point, final Runnable onSuccess) {
                        final ColorPicker picker = new ColorPicker();
                        picker.setColor(point.color);
                        picker.setListener(new ColorPickerAdapter() {
                            @Override
                            public void finished(Color newColor) {
                                point.color.set(newColor);
                                picker.dispose();
                                onSuccess.run();
                                result.getActor().fire(new GraphChangedEvent(false, true));
                            }

                            @Override
                            public void canceled(Color oldColor) {
                                picker.dispose();
                            }
                        });

                        gradientWidget.getStage().addActor(picker.fadeIn());
                    }
                });

        result.addGraphBoxPart(
                new GraphBoxPartImpl(
                        gradientWidget,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                Array<ColorPoint> points = gradientWidget.getPoints();
                                JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
                                for (ColorPoint point : points) {
                                    pointsValue.addChild(new JsonValue(point.color.toString() + "," + SimpleNumberFormatter.format(point.pos)));
                                }
                                object.addChild("points", pointsValue);
                            }
                        }
                ));

        final VisSelectBox<ClampMethod> clampMethodSelectBox = new VisSelectBox<ClampMethod>();
        clampMethodSelectBox.setItems(ClampMethod.values());
        if (data != null)
            clampMethodSelectBox.setSelected(ClampMethod.valueOf(data.getString("clamp", "Normal")));
        clampMethodSelectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.getActor().fire(new GraphChangedEvent(false, true));
                    }
                });

        VisTable clampActor = new VisTable();
        clampActor.add("Clamp method:").row();
        clampActor.add(clampMethodSelectBox).growX();

        result.addGraphBoxPart(
                new GraphBoxPartImpl(
                        clampActor,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                object.addChild("clamp", new JsonValue(clampMethodSelectBox.getSelected().name()));
                            }
                        }
                ));

        return result;
    }
}

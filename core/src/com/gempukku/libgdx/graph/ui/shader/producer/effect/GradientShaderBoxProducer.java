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
import com.gempukku.libgdx.ui.gradient.DefaultGradientDefinition;
import com.gempukku.libgdx.ui.gradient.GGradientEditor;
import com.gempukku.libgdx.ui.gradient.GradientDefinition;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class GradientShaderBoxProducer extends GraphBoxProducerImpl {
    public GradientShaderBoxProducer() {
        super(new GradientShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        DefaultGradientDefinition gradientDefinition = new DefaultGradientDefinition();
        if (data != null) {
            JsonValue points = data.get("points");
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                gradientDefinition.addColor(Float.parseFloat(split[1]), Color.valueOf(split[0]));
            }
        } else {
            gradientDefinition.addColor(0, Color.WHITE);
        }

        final GGradientEditor gradientEditor = new GGradientEditor(gradientDefinition, "gdx-graph");
        gradientEditor.setPrefWidth(300);
        gradientEditor.setPrefHeight(40);

        gradientEditor.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        gradientEditor.fire(new GraphChangedEvent(false, true));
                    }
                });

        result.addGraphBoxPart(
                new GraphBoxPartImpl(
                        gradientEditor,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                Array<GradientDefinition.ColorPosition> points = gradientEditor.getGradientDefinition().getColorPositions();
                                JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
                                for (GradientDefinition.ColorPosition point : points) {
                                    pointsValue.addChild(new JsonValue(point.color.toString() + "," + SimpleNumberFormatter.format(point.position)));
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

package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapValueShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.talosvfx.talos.editor.widgets.CurveWidget;

public class RemapValueShaderBoxProducer extends GraphBoxProducerImpl {
    public RemapValueShaderBoxProducer() {
        super(new RemapValueShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        final CurveWidget curveWidget = new CurveWidget(skin);

        if (data != null) {
            JsonValue points = data.get("points");
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                curveWidget.createPoint(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
        } else {
            curveWidget.createPoint(0, 0);
        }
        curveWidget.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        curveWidget.fire(new GraphChangedEvent(false, true));
                    }
                });

        curveWidget.setSize(300, 200);
        result.addGraphBoxPart(
                new GraphBoxPartImpl(
                        curveWidget,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                Array<Vector2> points = curveWidget.getPoints();
                                JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
                                for (Vector2 point : points) {
                                    pointsValue.addChild(new JsonValue(SimpleNumberFormatter.format(point.x) + "," + SimpleNumberFormatter.format(point.y)));
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
                        clampMethodSelectBox.fire(new GraphChangedEvent(false, true));
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

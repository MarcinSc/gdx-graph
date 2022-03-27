package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapVectorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class RemapVectorShaderBoxProducer extends GraphBoxProducerImpl {
    public RemapVectorShaderBoxProducer() {
        super(new RemapVectorShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        String x = "X";
        String y = "Y";
        String z = "Z";
        String w = "W";
        if (data != null) {
            x = data.getString("x", x);
            y = data.getString("y", y);
            z = data.getString("z", z);
            w = data.getString("W", w);
        }

        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);

        final VisSelectBox<String> xBox = createSelectBox(x);
        final VisSelectBox<String> yBox = createSelectBox(y);
        final VisSelectBox<String> zBox = createSelectBox(z);
        final VisSelectBox<String> wBox = createSelectBox(w);

        VisTable table = new VisTable();
        table.add("X: ");
        table.add(xBox);
        table.add("Y: ");
        table.add(yBox);
        table.row();
        table.add("Z: ");
        table.add(zBox);
        table.add("W: ");
        table.add(wBox);
        table.row();

        result.addGraphBoxPart(
                new GraphBoxPartImpl(
                        table,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                object.addChild("x", new JsonValue(xBox.getSelected()));
                                object.addChild("y", new JsonValue(yBox.getSelected()));
                                object.addChild("z", new JsonValue(zBox.getSelected()));
                                object.addChild("w", new JsonValue(wBox.getSelected()));
                            }
                        }
                ));

        return result;
    }

    private VisSelectBox<String> createSelectBox(String defaultValue) {
        final VisSelectBox<String> result = new VisSelectBox<String>();
        result.setItems("0.0", "1.0", "X", "Y", "Z", "W");
        result.setSelected(defaultValue);
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.fire(new GraphChangedEvent(false, true));
                    }
                });
        return result;
    }

}

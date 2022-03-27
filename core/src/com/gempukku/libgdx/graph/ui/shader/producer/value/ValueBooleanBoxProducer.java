package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.kotcrab.vis.ui.widget.VisCheckBox;


public class ValueBooleanBoxProducer extends ValueGraphBoxProducer {
    public ValueBooleanBoxProducer(NodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        boolean v = data.getBoolean("v");

        return createGraphBox(id, v);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(id, false);
    }

    private GraphBox createGraphBox(String id, boolean v) {
        GraphBoxImpl end = new GraphBoxImpl(id, configuration);
        end.addGraphBoxPart(createValuePart(v));

        return end;
    }

    private GraphBoxPartImpl createValuePart(boolean v) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        final VisCheckBox checkBox = new VisCheckBox("Value");
        checkBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        checkBox.fire(new GraphChangedEvent(false, true));
                    }
                });
        checkBox.setChecked(v);
        horizontalGroup.addActor(checkBox);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v", new JsonValue(checkBox.isChecked()));
                    }
                });
        colorPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}

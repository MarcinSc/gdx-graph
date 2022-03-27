package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class ValueVector2BoxProducer extends ValueGraphBoxProducer {
    public ValueVector2BoxProducer(NodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        float v1 = data.getFloat("v1");
        float v2 = data.getFloat("v2");

        return createGraphBox(id, v1, v2);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(id, 0, 0);
    }

    private GraphBox createGraphBox(String id, float v1, float v2) {
        GraphBoxImpl end = new GraphBoxImpl(id, configuration);
        end.addGraphBoxPart(createValuePart(v1, v2));

        return end;
    }

    private GraphBoxPartImpl createValuePart(float v1, float v2) {
        final VisValidatableTextField v1Input = new VisValidatableTextField(Validators.FLOATS) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(v1));
        v1Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v1Input.fire(new GraphChangedEvent(false, true));
                    }
                });

        final VisValidatableTextField v2Input = new VisValidatableTextField(Validators.FLOATS) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v2Input.setText(String.valueOf(v2));
        v2Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v2Input.fire(new GraphChangedEvent(false, true));
                    }
                });

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new VisLabel("x"));
        horizontalGroup.addActor(v1Input);
        horizontalGroup.addActor(new VisLabel("y"));
        horizontalGroup.addActor(v2Input);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v1", new JsonValue(Float.parseFloat(v1Input.getText())));
                        object.addChild("v2", new JsonValue(Float.parseFloat(v2Input.getText())));
                    }
                });
        colorPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}

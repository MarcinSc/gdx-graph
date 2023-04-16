package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class ValueFloatBoxProducerDefault extends ValueGraphBoxProducerDefault {
    public ValueFloatBoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart(Skin skin) {
        float v1 = 0;
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

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new VisLabel("x"));
        horizontalGroup.addActor(v1Input);

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(horizontalGroup,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        float value;
                        try {
                            value = Float.parseFloat(v1Input.getText());
                        } catch (NumberFormatException exp) {
                            value = 0f;
                        }
                        object.addChild("v1", new JsonValue(value));
                    }
                }) {
            @Override
            public void initialize(JsonValue data) {
                if (data != null)
                    v1Input.setText(String.valueOf(data.getFloat("v1", 0)));
            }
        };
        colorPart.setOutputConnector(GraphNodeEditorOutput.Side.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}

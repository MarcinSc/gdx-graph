package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class ValueVector2BoxProducerDefault extends ValueGraphBoxProducerDefault {
    public ValueVector2BoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        final VisValidatableTextField v1Input = ValueVectorCreator.createValueInput(0);
        final VisValidatableTextField v2Input = ValueVectorCreator.createValueInput(0);

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new VisLabel("x"));
        horizontalGroup.addActor(v1Input);
        horizontalGroup.addActor(new VisLabel("y"));
        horizontalGroup.addActor(v2Input);

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(horizontalGroup,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v1", new JsonValue(Float.parseFloat(v1Input.getText())));
                        object.addChild("v2", new JsonValue(Float.parseFloat(v2Input.getText())));
                    }
                }) {
            @Override
            public void initialize(JsonValue data) {
                if (data != null) {
                    v1Input.setText(String.valueOf(data.getFloat("v1", 0)));
                    v2Input.setText(String.valueOf(data.getFloat("v2", 0)));
                }
            }
        };
        colorPart.setOutputConnector(GraphNodeEditorOutput.Side.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}

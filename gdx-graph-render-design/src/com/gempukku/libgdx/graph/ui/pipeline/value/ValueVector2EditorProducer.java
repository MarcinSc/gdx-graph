package com.gempukku.libgdx.graph.ui.pipeline.value;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class ValueVector2EditorProducer extends ValueGraphEditorProducer {
    public ValueVector2EditorProducer(MenuNodeConfiguration configuration) {
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
                    public void initialize(JsonValue data) {
                        if (data != null) {
                            v1Input.setText(String.valueOf(data.getFloat("v1", 0)));
                            v2Input.setText(String.valueOf(data.getFloat("v2", 0)));
                        }
                    }

                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v1", new JsonValue(Float.parseFloat(v1Input.getText())));
                        object.addChild("v2", new JsonValue(Float.parseFloat(v2Input.getText())));
                    }
                });
        GraphNodeOutput output = configuration.getNodeOutputs().get("value");
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, output, getOutputDrawable(output, true), getOutputDrawable(output, false));
        return colorPart;
    }
}

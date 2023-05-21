package com.gempukku.libgdx.graph.ui.pipeline.value;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.undo.UndoableCheckBox;
import com.kotcrab.vis.ui.widget.VisCheckBox;


public class ValueBooleanEditorProducer extends ValueGraphEditorProducer {
    public ValueBooleanEditorProducer(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        boolean v = false;
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        final VisCheckBox checkBox = new UndoableCheckBox("Value", "gdx-graph-property-label");
        checkBox.setChecked(v);
        horizontalGroup.addActor(checkBox);

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(horizontalGroup,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void initialize(JsonValue data) {
                        if (data != null)
                            checkBox.setChecked(data.getBoolean("v", false));
                    }

                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v", new JsonValue(checkBox.isChecked()));
                    }
                });
        GraphNodeOutput output = configuration.getNodeOutputs().get("value");
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, output, getOutputDrawable(output, true), getOutputDrawable(output, false));
        return colorPart;
    }
}

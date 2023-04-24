package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisCheckBox;


public class ValueBooleanBoxProducerDefault extends ValueGraphBoxProducerDefault {
    public ValueBooleanBoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        boolean v = false;
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        final VisCheckBox checkBox = new VisCheckBox("Value", "gdx-graph-property-label");
        checkBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        checkBox.fire(new GraphChangedEvent(false, true));
                    }
                });
        checkBox.setChecked(v);
        horizontalGroup.addActor(checkBox);

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(horizontalGroup,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("v", new JsonValue(checkBox.isChecked()));
                    }
                }) {
            @Override
            public void initialize(JsonValue data) {
                if (data != null)
                    checkBox.setChecked(data != null && data.getBoolean("v", false));
            }
        };
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}

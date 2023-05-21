package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.gempukku.libgdx.ui.undo.UndoableValidatableTextField;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class ValueVectorCreator {
    public static VisValidatableTextField createValueInput(float defaultValue) {
        final VisValidatableTextField input = new UndoableValidatableTextField(String.valueOf(defaultValue), "gdx-graph-property") {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        input.addValidator(Validators.FLOATS);
        return input;
    }
}

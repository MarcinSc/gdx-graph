package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class ValueVectorCreator {
    public static VisValidatableTextField createValueInput(float defaultValue) {
        final VisValidatableTextField input = new VisValidatableTextField(String.valueOf(defaultValue), "gdx-graph-property") {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        input.addValidator(Validators.FLOATS);
        input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        input.fire(new GraphChangedEvent(false, true));
                    }
                });
        return input;
    }
}

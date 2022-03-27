package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class IntegerBoxPart extends VisTable implements GraphBoxPart {
    private String property;
    private final VisValidatableTextField v1Input;

    public IntegerBoxPart(String label, String property, int defaultValue, InputValidator inputValidator) {
        this.property = property;
        v1Input = new VisValidatableTextField(Validators.INTEGERS, inputValidator) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(defaultValue));
        v1Input.setAlignment(Align.right);
        v1Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v1Input.fire(new GraphChangedEvent(false, true));
                    }
                });

        add(new VisLabel(label));
        add(v1Input).growX();
        row();
    }


    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    public void initialize(JsonValue data) {
        if (data != null) {
            int value = data.getInt(property);
            v1Input.setText(String.valueOf(value));
        }
    }

    public void setValue(float value) {
        v1Input.setText(String.valueOf(value));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(Integer.parseInt(v1Input.getText())));
    }

    @Override
    public void dispose() {

    }
}

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

public class Vector3BoxPart extends VisTable implements GraphBoxPart {
    private String propertyX;
    private String propertyY;
    private String propertyZ;
    private final VisValidatableTextField xInput;
    private final VisValidatableTextField yInput;
    private final VisValidatableTextField zInput;

    public Vector3BoxPart(String label, String propertyX, String propertyY, String propertyZ,
                          float defaultX, float defaultY, float defaultZ,
                          InputValidator inputValidatorX, InputValidator inputValidatorY, InputValidator inputValidatorZ) {
        this.propertyX = propertyX;
        this.propertyY = propertyY;
        this.propertyZ = propertyZ;
        xInput = createInput(inputValidatorX, defaultX);
        yInput = createInput(inputValidatorY, defaultY);
        zInput = createInput(inputValidatorZ, defaultZ);

        add(new VisLabel(label));
        add(xInput).growX();
        add(yInput).growX();
        add(zInput).growX();
        row();
    }

    private VisValidatableTextField createInput(InputValidator inputValidator, float defaultValue) {
        final VisValidatableTextField result;
        if (inputValidator != null) {
            result = new VisValidatableTextField(Validators.FLOATS, inputValidator) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        } else
            result = new VisValidatableTextField(Validators.FLOATS) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        result.setText(String.valueOf(defaultValue));
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.fire(new GraphChangedEvent(false, true));
                    }
                });
        return result;
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
            float valueX = data.getFloat(propertyX);
            float valueY = data.getFloat(propertyY);
            float valueZ = data.getFloat(propertyZ);
            xInput.setText(String.valueOf(valueX));
            yInput.setText(String.valueOf(valueY));
            zInput.setText(String.valueOf(valueZ));
        }
    }

    public void setValue(float x, float y, float z) {
        xInput.setText(String.valueOf(x));
        yInput.setText(String.valueOf(y));
        zInput.setText(String.valueOf(z));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(propertyX, new JsonValue(Float.parseFloat(xInput.getText())));
        object.addChild(propertyY, new JsonValue(Float.parseFloat(yInput.getText())));
        object.addChild(propertyZ, new JsonValue(Float.parseFloat(zInput.getText())));
    }

    @Override
    public void dispose() {

    }
}

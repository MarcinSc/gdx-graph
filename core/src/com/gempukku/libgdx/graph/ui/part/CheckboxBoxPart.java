package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;


public class CheckboxBoxPart extends VisTable implements GraphBoxPart {
    private String property;
    private final VisCheckBox input;

    public CheckboxBoxPart(String label, String property) {
        this(label, property, false);
    }

    public CheckboxBoxPart(String label, String property, boolean selected) {
        this.property = property;

        input = new VisCheckBox(label);
        input.setChecked(selected);

        add(input).left().grow();
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
            input.setChecked(data.getBoolean(property, false));
        }
    }

    public void setValue(boolean value) {
        input.setChecked(value);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.isChecked()));
    }

    @Override
    public void dispose() {

    }
}

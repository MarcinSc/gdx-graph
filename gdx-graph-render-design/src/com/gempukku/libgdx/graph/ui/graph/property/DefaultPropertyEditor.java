package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.gempukku.libgdx.ui.undo.UndoableTextField;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class DefaultPropertyEditor extends VisTable implements PropertyEditor {
    private String propertyType;
    private Array<GraphNodeEditorPart> propertyEditorParts = new Array<>();
    private VisTextField nameField;

    public DefaultPropertyEditor(String name, String propertyType) {
        this.propertyType = propertyType;

        nameField = new UndoableTextField(name, "gdx-graph-property");
        VisTable headerTable = new VisTable();
        headerTable.add(new VisLabel("Name: ", "gdx-graph-property-label"));
        headerTable.add(nameField).growX();
        headerTable.row();
        add(headerTable).growX().row();
    }

    @Override
    public String getType() {
        return propertyType;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphNodeEditorPart graphEditorPart : propertyEditorParts)
            graphEditorPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    public void addPropertyEditorPart(GraphNodeEditorPart graphEditorPart) {
        propertyEditorParts.add(graphEditorPart);
        final Actor actor = graphEditorPart.getActor();
        add(actor).growX().row();
    }

    public void initialize(JsonValue value) {
        for (GraphNodeEditorPart graphEditorPart : propertyEditorParts) {
            graphEditorPart.initialize(value);
        }
    }

    @Override
    public Actor getActor() {
        return this;
    }
}

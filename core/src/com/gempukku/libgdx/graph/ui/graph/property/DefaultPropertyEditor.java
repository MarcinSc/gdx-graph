package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.gempukku.libgdx.ui.undo.UndoableTextField;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class DefaultPropertyEditor extends VisTable implements PropertyEditor {
    private String propertyType;
    private Array<GraphNodeEditorPart> propertyEditorParts = new Array<>();
    private VisTextField nameField;
    private PropertyLocation[] propertyLocations;
    private EnumSelectEditorPart<PropertyLocation> locationPart;

    public DefaultPropertyEditor(String name, String propertyType,
                                 PropertyLocation selectedLocation,
                                 PropertyLocation... propertyLocations) {
        this.propertyType = propertyType;

        if (propertyLocations.length>0) {
            if (selectedLocation == null)
                selectedLocation = propertyLocations[0];
            locationPart = new EnumSelectEditorPart<>("Location", "location", selectedLocation, new ToStringEnum<>(), new Array<>(propertyLocations));
        }

        nameField = new UndoableTextField(name, "gdx-graph-property");
        this.propertyLocations = propertyLocations;
        VisTable headerTable = new VisTable();
        headerTable.add(new VisLabel("Name: ", "gdx-graph-property-label"));
        headerTable.add(nameField).growX();
        headerTable.row();
        add(headerTable).growX().row();

        if (propertyLocations.length > 1)
            add(locationPart).growX().row();
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
    public PropertyLocation getLocation() {
        if (propertyLocations.length > 0)
            return PropertyLocation.valueOf(locationPart.getSelected());
        return null;
    }

    @Override
    public Actor getActor() {
        return this;
    }
}

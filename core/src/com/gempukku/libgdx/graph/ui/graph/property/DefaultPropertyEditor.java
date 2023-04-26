package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
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

        locationPart = new EnumSelectEditorPart<>("Location", "location", new ToStringEnum<>(), propertyLocations);
        if (selectedLocation != null)
            locationPart.setSelected(selectedLocation);

        nameField = new VisTextField(name);
        this.propertyLocations = propertyLocations;
        VisTable headerTable = new VisTable();
        headerTable.add(new VisLabel("Name: "));
        headerTable.add(nameField).growX();
        nameField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(true, true));
                    }
                });
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

    @Override
    public void dispose() {
        for (GraphNodeEditorPart graphEditorPart : propertyEditorParts) {
            if (graphEditorPart instanceof Disposable) {
                ((Disposable) graphEditorPart).dispose();
            }
        }
    }
}

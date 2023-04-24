package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector3EditorPart;

public class PropertyVector3EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Vector3";
    }

    @Override
    public String getDefaultName() {
        return "New Vector3";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.Vector3, location, propertyLocations);
        result.addPropertyBoxPart(new Vector3EditorPart("Vector3",
                "x", "y", "z",
                0, 0, 0,
                null, null, null));
        result.initialize(jsonObject);
        return result;
    }
}

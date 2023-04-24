package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.ColorEditorPart;


public class PropertyColorEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Vector4";
    }

    @Override
    public String getDefaultName() {
        return "New Color";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.Vector4, location, propertyLocations);
        result.addPropertyBoxPart(new ColorEditorPart("Color", "color"));
        result.initialize(jsonObject);

        return result;
    }
}

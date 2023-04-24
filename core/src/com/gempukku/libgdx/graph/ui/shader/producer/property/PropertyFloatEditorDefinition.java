package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;


public class PropertyFloatEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Float";
    }

    @Override
    public String getDefaultName() {
        return "New Float";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue data, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.Float, location, propertyLocations);
        result.addPropertyBoxPart(new FloatEditorPart("Value", "x", 0, null));
        if (data != null)
            result.initialize(data);

        return result;
    }
}

package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector4EditorPart;

public class PropertyMatrix4EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Matrix4";
    }

    @Override
    public String getDefaultName() {
        return "New Matrix4";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.Matrix4, location, propertyLocations);
        result.addPropertyBoxPart(new Vector4EditorPart("",
                "x1", "y1", "z1", "w1",
                1, 0, 0, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4EditorPart("",
                "x2", "y2", "z2", "w2",
                0, 1, 0, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4EditorPart("",
                "x3", "y3", "z3", "w3",
                0, 0, 1, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4EditorPart("",
                "x4", "y4", "z4", "w4",
                0, 0, 0, 1,
                null, null, null, null));
        result.initialize(jsonObject);
        return result;
    }
}

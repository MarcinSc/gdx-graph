package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector2EditorPart;

public class PropertyVector2EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Vector2";
    }

    @Override
    public String getDefaultName() {
        return "New Vector2";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.Vector2, location, propertyLocations);
        result.addPropertyBoxPart(new Vector2EditorPart("Vector2", "x", "y", 0, 0, null, null));
        result.initialize(jsonObject);
        return result;
    }
}

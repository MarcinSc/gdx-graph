package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.List;

public interface ShaderPropertyEditorDefinition extends PropertyEditorDefinition {
    void setPropertyFunctions(List<String> propertyFunctions);

    @Override
    ShaderPropertyEditor createPropertyEditor(String name, JsonValue data);
}

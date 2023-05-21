package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.utils.JsonValue;

public interface PropertyEditorDefinition {
    String getType();

    String getDefaultName();

    PropertyEditor createPropertyEditor(String name, JsonValue data);

    Iterable<? extends PropertyGraphEditorCustomization> getCustomizations();
}

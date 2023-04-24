package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public interface PropertyEditorDefinition {
    String getType();

    String getDefaultName();

    PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue data, PropertyLocation[] propertyLocations);

    Iterable<? extends PropertyGraphBoxCustomization> getCustomizations();
}

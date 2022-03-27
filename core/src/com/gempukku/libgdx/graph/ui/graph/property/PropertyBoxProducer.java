package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public interface PropertyBoxProducer {
    String getType();

    PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations);

    PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations);
}

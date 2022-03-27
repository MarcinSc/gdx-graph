package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;


public class PropertyColorBoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return "Vector4";
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, ShaderFieldType.Vector4, location, propertyLocations);
        result.addPropertyBoxPart(new ColorBoxPart("Color", "color"));
        result.initialize(jsonObject);

        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations) {
        return createPropertyBox(skin, "New Color", null, null, propertyLocations);
    }
}

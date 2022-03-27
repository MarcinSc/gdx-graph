package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.Vector4BoxPart;

public class PropertyMatrix4BoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return "Matrix4";
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, ShaderFieldType.Matrix4, location, propertyLocations);
        result.addPropertyBoxPart(new Vector4BoxPart("",
                "x1", "y1", "z1", "w1",
                1, 0, 0, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4BoxPart("",
                "x2", "y2", "z2", "w2",
                0, 1, 0, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4BoxPart("",
                "x3", "y3", "z3", "w3",
                0, 0, 1, 0,
                null, null, null, null));
        result.addPropertyBoxPart(new Vector4BoxPart("",
                "x4", "y4", "z4", "w4",
                0, 0, 0, 1,
                null, null, null, null));
        result.initialize(jsonObject);
        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations) {
        return createPropertyBox(skin, "New Matrix4", null, null, propertyLocations);
    }
}

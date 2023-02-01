package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.FileSelectorBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;

public class PropertyTextureBoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return "TextureRegion";
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, ShaderFieldType.TextureRegion, location, propertyLocations);
        result.addPropertyBoxPart(new EnumSelectBoxPart<>("Min filter ", "minFilter",
                new StringifyEnum<Texture.TextureFilter>(), Texture.TextureFilter.values()));
        result.addPropertyBoxPart(new EnumSelectBoxPart<>("Mag filter ", "magFilter",
                new StringifyEnum<Texture.TextureFilter>(), Texture.TextureFilter.values()));
        result.addPropertyBoxPart(new FileSelectorBoxPart("Preview texture ", "previewPath"));
        result.initialize(jsonObject);

        result.addPropertyGraphBoxCustomization(new TextureCustomization());

        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin, PropertyLocation[] propertyLocations) {
        return createPropertyBox(skin, "New Texture", null, null, propertyLocations);
    }
}

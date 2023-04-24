package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphBoxCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.FileSelectorEditorPart;

public class PropertyTextureEditorDefinition implements PropertyEditorDefinition {
    private Array<PropertyGraphBoxCustomization> customizations = new Array<>();

    public PropertyTextureEditorDefinition() {
        customizations.add(new TextureCustomization());
    }

    @Override
    public String getType() {
        return "TextureRegion";
    }

    @Override
    public String getDefaultName() {
        return "New Texture";
    }

    @Override
    public Iterable<? extends PropertyGraphBoxCustomization> getCustomizations() {
        return customizations;
    }

    @Override
    public PropertyBox createPropertyBox(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyBox result = new DefaultPropertyBox(name, ShaderFieldType.TextureRegion, location, propertyLocations);
        result.addPropertyBoxPart(new EnumSelectEditorPart<>("Min filter ", "minFilter",
                new TextureFilterDisplayText(), Texture.TextureFilter.values()));
        result.addPropertyBoxPart(new EnumSelectEditorPart<>("Mag filter ", "magFilter",
                new TextureFilterDisplayText(), Texture.TextureFilter.values()));
        result.addPropertyBoxPart(new FileSelectorEditorPart("Preview texture ", "previewPath"));
        result.initialize(jsonObject);

        return result;
    }
}

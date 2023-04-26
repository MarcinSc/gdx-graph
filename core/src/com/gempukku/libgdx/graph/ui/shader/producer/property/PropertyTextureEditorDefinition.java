package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.FileSelectorEditorPart;

public class PropertyTextureEditorDefinition implements PropertyEditorDefinition {
    private Array<PropertyGraphEditorCustomization> customizations = new Array<>();

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
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return customizations;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, PropertyLocation location, JsonValue jsonObject, PropertyLocation[] propertyLocations) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, ShaderFieldType.TextureRegion, location, propertyLocations);
        result.addPropertyEditorPart(new EnumSelectEditorPart<>("Min filter ", "minFilter",
                new TextureFilterDisplayText(), Texture.TextureFilter.values()));
        result.addPropertyEditorPart(new EnumSelectEditorPart<>("Mag filter ", "magFilter",
                new TextureFilterDisplayText(), Texture.TextureFilter.values()));
        result.addPropertyEditorPart(new FileSelectorEditorPart("Preview texture ", "previewPath"));
        result.initialize(jsonObject);

        return result;
    }
}

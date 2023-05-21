package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.graph.ui.pipeline.property.TextureCustomization;
import com.gempukku.libgdx.graph.ui.pipeline.property.TextureFilterDisplayText;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.FileSelectorEditorPart;

import java.util.List;

public class ShaderPropertyTextureEditorDefinition implements ShaderPropertyEditorDefinition {
    private Array<PropertyGraphEditorCustomization> customizations = new Array<>();
    private List<String> propertyFunctions;

    public ShaderPropertyTextureEditorDefinition() {
        customizations.add(new TextureCustomization());
    }

    @Override
    public String getType() {
        return ShaderFieldType.TextureRegion;
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
    public void setPropertyFunctions(List<String> propertyFunctions) {
        this.propertyFunctions = propertyFunctions;
    }

    @Override
    public ShaderPropertyEditor createPropertyEditor(String name, JsonValue data) {
        DefaultShaderPropertyEditor result = new DefaultShaderPropertyEditor(name, getType(), propertyFunctions);
        result.addPropertyEditorPart(new EnumSelectEditorPart<>("Min filter ", "minFilter", Texture.TextureFilter.Nearest,
                new TextureFilterDisplayText(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(Texture.TextureFilter.values())));
        result.addPropertyEditorPart(new EnumSelectEditorPart<>("Mag filter ", "magFilter", Texture.TextureFilter.Nearest,
                new TextureFilterDisplayText(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(Texture.TextureFilter.values())));
        result.addPropertyEditorPart(new FileSelectorEditorPart("Preview texture ", "previewPath", Gdx.files.local(".")));
        result.initialize(data);

        return result;
    }
}

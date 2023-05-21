package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.ColorPickerSupplier;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.ColorEditorPart;

import java.util.List;


public class ShaderPropertyColorEditorDefinition implements ShaderPropertyEditorDefinition {
    private List<String> propertyFunctions;

    @Override
    public String getType() {
        return ShaderFieldType.Vector4;
    }

    @Override
    public String getDefaultName() {
        return "New Color";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public void setPropertyFunctions(List<String> propertyFunctions) {
        this.propertyFunctions = propertyFunctions;
    }

    @Override
    public ShaderPropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultShaderPropertyEditor result = new DefaultShaderPropertyEditor(name, getType(), propertyFunctions);
        result.addPropertyEditorPart(new ColorEditorPart(ColorPickerSupplier.instance, "Color", "color", Color.WHITE, "gdx-graph-property-label"));
        result.initialize(jsonObject);

        return result;
    }
}

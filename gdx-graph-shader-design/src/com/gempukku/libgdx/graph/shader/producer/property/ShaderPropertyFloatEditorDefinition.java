package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;

import java.util.List;

public class ShaderPropertyFloatEditorDefinition implements ShaderPropertyEditorDefinition {
    private List<String> propertyFunctions;

    @Override
    public String getType() {
        return ShaderFieldType.Float;
    }

    @Override
    public String getDefaultName() {
        return "New Float";
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
    public ShaderPropertyEditor createPropertyEditor(String name, JsonValue data) {
        DefaultShaderPropertyEditor result = new DefaultShaderPropertyEditor(name, getType(), propertyFunctions);
        result.addPropertyEditorPart(new FloatEditorPart("Value", "x", 0, null, "gdx-graph-property-label", "gdx-graph-property"));
        if (data != null)
            result.initialize(data);

        return result;
    }
}

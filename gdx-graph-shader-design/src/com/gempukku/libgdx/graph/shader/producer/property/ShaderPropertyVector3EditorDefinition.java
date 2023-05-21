package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector3EditorPart;

import java.util.List;

public class ShaderPropertyVector3EditorDefinition implements ShaderPropertyEditorDefinition {
    private List<String> propertyFunctions;

    @Override
    public String getType() {
        return ShaderFieldType.Vector3;
    }

    @Override
    public String getDefaultName() {
        return "New Vector3";
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
        result.addPropertyEditorPart(new Vector3EditorPart("Vector3",
                "x", "y", "z",
                0, 0, 0,
                null, null, null,
                "gdx-graph-property-label", "gdx-graph-property"));
        result.initialize(data);
        return result;
    }
}

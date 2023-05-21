package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector4EditorPart;

import java.util.List;

public class ShaderPropertyMatrix4EditorDefinition implements ShaderPropertyEditorDefinition {
    private List<String> propertyFunctions;

    @Override
    public String getType() {
        return ShaderFieldType.Matrix4;
    }

    @Override
    public String getDefaultName() {
        return "New Matrix4";
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
        result.addPropertyEditorPart(new Vector4EditorPart("",
                "x1", "y1", "z1", "w1",
                1, 0, 0, 0,
                null, null, null, null, "gdx-graph-property-label", "gdx-graph-property"));
        result.addPropertyEditorPart(new Vector4EditorPart("",
                "x2", "y2", "z2", "w2",
                0, 1, 0, 0,
                null, null, null, null, "gdx-graph-property-label", "gdx-graph-property"));
        result.addPropertyEditorPart(new Vector4EditorPart("",
                "x3", "y3", "z3", "w3",
                0, 0, 1, 0,
                null, null, null, null, "gdx-graph-property-label", "gdx-graph-property"));
        result.addPropertyEditorPart(new Vector4EditorPart("",
                "x4", "y4", "z4", "w4",
                0, 0, 0, 1,
                null, null, null, null, "gdx-graph-property-label", "gdx-graph-property"));
        result.initialize(data);
        return result;
    }
}

package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector3EditorPart;

public class PropertyVector3EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Vector3";
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
    public PropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, ShaderFieldType.Vector3);
        result.addPropertyEditorPart(new Vector3EditorPart("Vector3",
                "x", "y", "z",
                0, 0, 0,
                null, null, null,
                "gdx-graph-property-label", "gdx-graph-property"));
        result.initialize(jsonObject);
        return result;
    }
}

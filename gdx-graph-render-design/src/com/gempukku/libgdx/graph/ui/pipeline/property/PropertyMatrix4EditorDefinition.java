package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector4EditorPart;

public class PropertyMatrix4EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Matrix4";
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
    public PropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, getType());
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
        result.initialize(jsonObject);
        return result;
    }
}

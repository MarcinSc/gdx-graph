package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;


public class PropertyFloatEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Float";
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
    public PropertyEditor createPropertyEditor(String name, JsonValue data) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, getType());
        result.addPropertyEditorPart(new FloatEditorPart("Value", "x", 0, null, "gdx-graph-property-label", "gdx-graph-property"));
        if (data != null)
            result.initialize(data);

        return result;
    }
}

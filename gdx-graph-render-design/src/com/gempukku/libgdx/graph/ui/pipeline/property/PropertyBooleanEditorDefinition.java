package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;

public class PropertyBooleanEditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public String getDefaultName() {
        return "New Boolean";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, ShaderFieldType.Vector4);
        result.addPropertyEditorPart(new CheckboxEditorPart("Value", "value", false, "gdx-graph-property-label"));
        result.initialize(jsonObject);

        return result;
    }
}
